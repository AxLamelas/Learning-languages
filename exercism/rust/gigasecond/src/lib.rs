extern crate chrono;

use chrono::*;
use std::i64;



// Returns a Utc DateTime one billion seconds after start.

pub fn after(start: DateTime<Utc>) -> DateTime<Utc> {
    start.checked_add_signed( Duration::seconds((10 as i64).pow(9))).unwrap()
}

