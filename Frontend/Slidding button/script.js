var outside = document.querySelector(".outside");
outside.addEventListener("click",changeSide);
var right = false;
var margin = 0.2; //rem
outside.querySelector(".inside").style.left = margin + "rem";

function changeSide(){
    var slide = outside.querySelector(".inside");
    if (right) {
        slide.style.left = margin + "rem";
    } else {
        var distance = outside.clientWidth - slide.clientWidth - margin*parseFloat(getComputedStyle(document.body).fontSize);
        console.log(distance) 
        slide.style.left = distance + "px";
    }
    right = !right;
    
}