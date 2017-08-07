var outside = document.querySelector(".outside");
var inside = outside.querySelector(".inside");
var maxWidth = parseFloat(getComputedStyle(outside).width)- parseFloat(getComputedStyle(inside).width);
var refOutside = parseFloat(getComputedStyle(outside)["margin-left"]);
console.log(refOutside)


outside.addEventListener("mousedown", down);

function down() {
    outside.addEventListener("mousemove",move);
    document.addEventListener("mouseup", remove);
}

function move(e){    
    if ( e.clientX > refOutside + maxWidth){
        inside.style.left = maxWidth + "px";
    } else if (e.clientX < refOutside) {
        inside.style.left = 0 + "px";
    } else {
        inside.style.left = e.clientX + "px";
    }
    document.getElementById("value").innerHTML = calcPercentage();
}

function calcPercentage(){
    var pos = parseFloat(getComputedStyle(inside).left);
    return roundD((pos-refOutside)/maxWidth * 100,1);
}


function remove() {
    outside.removeEventListener("mousemove",move);
    document.removeEventListener("mouseup", remove);
}

function roundD(x,nDecPlaces) {
    return Math.round(x*Math.pow(10,nDecPlaces)) / Math.pow(10,nDecPlaces);
}

