import sequtils

type
    Tensor[T] = object
        vals: seq[T]
        shape: seq[int]

#Helpers:
proc indexOfAll[T](list: openArray[T],val: T): seq[int] = 
    result = newSeq[int](0)
    for i,v in list:
        if v == val:
            result.add i


proc `^`[T: float | int](base: T; exponent: int): T =
    result = 1
    for i in 1..exponent:
        result *= base

    
proc `[]=`[T](list: var openArray[T], indexes: openArray[int], val: T) =
    for i in indexes:
        list[i] = val

proc `&`[A,B: static[int], T](a: array[A, T], b: array[B, T]): array[A+B,T] =    
    result[0..<A] = a
    result[A..<B] = b

proc `&`[T](a,b: seq[T]): seq[T]=
    result = newSeq[T](a.len + b.len)
    result[0..<a.len] = a
    result[a.len..<result.len] = b

proc sizeOfShape(shape: varargs[int]):int =
    result = 1
    for i in shape:
        result *= i


proc toTensor[T](list: openArray[T],shape: varargs[int]): Tensor[T] = 
    if list.len != sizeOfShape(shape):
        raise newException(ValueError, "The number of elements required for the introduced shape do not match the number of elements introduced")
    return Tensor[T](vals: @list, shape: @shape)

proc reshape[T](tensor: var Tensor[T], shape: varargs[int]) =
    if sizeOfShape(shape) != sizeOfShape(tensor.shape):
        raise newException(ValueError, "The number of elements required for the introduced shape do not match the number of elements in the tensor")
    
    tensor.shape = @shape


proc `[]`[T](tensor: Tensor[T],position: varargs[int]): Tensor[T] =
    let dirWithFullVals = position.indexOfAll(-1)

    if dirWithFullVals == toSeq(0..<position.len):
        return tensor

    var posOfSingleVals = @position
    posOfSingleVals[dirWithFullVals] = 0

    let shape = tensor.shape & @[1]  

    let start = (var x = 0; for i,_ in posOfSingleVals: x+= posOfSingleVals[i]*shape[i+1]; x)
    
    if @position == posOfSingleVals:
        return Tensor[T](vals: @[tensor.vals[start]], shape: @[])

    let length = (var y: uint = 1; for i in dirWithFullVals: y *= uint(shape[i]); y)
    let step = (var z = 0; for i in dirWithFullVals: z += shape[i]^(tensor.shape.len - 1 - i) ;z)

    var current = start
    var vals = newSeq[T](0)
    while vals.len < int(length):
        vals.add tensor.vals[current]
        current += step

    var newShape = newSeq[int](0)
    for i in dirWithFullVals:
        newShape.add shape[i]

    return Tensor[T](vals: vals, shape: newShape)

proc `[]=`[T](tensor: var Tensor[T], position: varargs[int], val: T) =
    let dirWithFullVals = position.indexOfAll(-1)
    
    if dirWithFullVals == toSeq(0..<position.len):
        for i,_ in tensor.vals:
            tensor.vals[i] = val
        return

    var posOfSingleVals = @position
    posOfSingleVals[dirWithFullVals] = 0

    let shape = tensor.shape & @[1]  

    let start = (var x = 0; for i,_ in posOfSingleVals: x+= posOfSingleVals[i]*shape[i+1]; x)
    
    if @position == posOfSingleVals:
        tensor.vals[start] = val
        return

    let length = (var y: uint = 1; for i in dirWithFullVals: y *= uint(shape[i]); y)
    let step = (var z = 0; for i in dirWithFullVals: z += shape[i]^(tensor.shape.len - 1 - i) ;z)

    var current = start
    var counter = 0

    while counter < int(length):
        counter += 1
        tensor.vals[current] = val        
        current += step

#Operation between a tensor and a scalar:

proc `+`[T](tensor: Tensor[T], val: T): Tensor[T] = 
    var newVals = newSeq[T](tensor.vals.len)
    for i,v in tensor.vals:
        newVals[i] = v + val
    
    return Tensor[T](vals: newVals, shape: tensor.shape)

proc `+`[T](val: T, tensor: Tensor[T]): Tensor[T] =
    return tensor + val 

proc `*`[T](tensor: Tensor[T], val: T): Tensor[T] = 
    var newVals = newSeq[T](tensor.vals.len)
    for i,v in tensor.vals:
        newVals[i] = v * val

    return Tensor[T](vals: newVals, shape: tensor.shape)

proc `*`[T](val: T, tensor: Tensor[T]): Tensor[T] =
    return tensor * val 

proc `-`[T](tensor: Tensor[T], val: T): Tensor[T] = 
    return tensor + (-1 * val)

proc `-`[T](val: T, tensor: Tensor[T]): Tensor[T] = 
    return (-1 * tensor) + val

proc `/`[T](tensor: Tensor[T], val: T): Tensor[T] =
    return tensor * (1/val)

#Operation between tensors:

proc `+`[T](a,b: Tensor[T]): Tensor[T] =
    if a.shape != b.shape:
        raise newException(ValueError, "The given tensors do not have the same shape")
    
    var newVals = newSeq[T](a.vals.len)
    for i,v in a.vals:
        newVals[i] = v + b.vals[i]

    return Tensor[T](vals: newVals, shape: a.shape)        

