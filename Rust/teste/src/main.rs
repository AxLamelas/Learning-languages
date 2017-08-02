extern crate gnuplot;

use gnuplot::{Figure, Caption, Color};


struct square_wave {
    ti: f64,
    T: f64

}

impl square_wave{
    fn get_status(&self,t: &f64) -> u8{       
        (t / self.T - (t / self.T).floor() < 0.5) as u8
    }
}

fn main() {
    let w1 = square_wave{ ti: 0.0, T: 1.0};
    let x = linspace(0.0,0.001,3.0);
    let y: Vec<f64> = x.iter().map(|v| w1.get_status(v) as f64).collect();
    let mut sum: f64 = 0.0;
    let sum: f64 = y.iter().sum();

    let mut fg = Figure::new();
    fg.axes2d().lines(&x, &y, &[Caption("A line"), Color("black")]);
    fg.show();

    println!("{  }",sum);
    println!("{}",y.len());

}


fn linspace(inicial: f64,step: f64,last: f64) -> Vec<f64>{
    let mut v = inicial;
    let mut vec = Vec::new();
    while v < last{
        vec.push(v);
        v +=step;
    }
    vec 
}