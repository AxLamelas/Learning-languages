extern crate num;

use num::*;

fn main() {
    let mut x = 0.0;
    let e = eventListener{parameter: &x,event: event,callback: callback };
    while true {
        x += 1.0;
        e.check();
    }
}

fn callback(){
    println!("Event happened");
}

fn event<A: Float + >(val: &A) -> bool
{
    match val {
        f64 => val > 50.0,
        i64 => val > 40,
    }
}




#[derive(Debug)]

struct eventListener<'a, A>
{
    parameter: &'a A,
    event: fn(&'a A) -> bool,
    callback: fn()
}

impl<'a,A> eventListener<'a,A>
{
    fn check(&self) {
        if (self.event)(self.parameter) {
            (self.callback)();
        }  
    }
}

