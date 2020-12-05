
var min=1, max=8;

function getRandomInRange(min, max) {
  return Math.floor(Math.random() * (max - min + 1)) + min;
}
var game, part;
game = getRandomInRange(min,max);

switch(game){
	case 1: part="Hotel"; break;
	case 2: part="Roots"; break;
	case 3: part="Seasons"; break;
	case 4: part="The Lake"; break;
	case 5: part="Birthday"; break;
	case 6: part="Arles"; break;
	case 7: part="Case 23"; break;
	case 8: part="The Mill"; break;
	default: console.log("error");
}

document.addEventListener('DOMContentLoaded',function(){
var h3 = document.getElementById('for_game');
h3.textContent = part;
h3.style.color ='yellow';
h3.style.fontSize = '75px';
});



document.querySelectorAll(".nav-link").forEach(function(e) {

    e.addEventListener("pointerenter", function(event) {
        var targetElement = event.target || event.srcElement;
        targetElement.style.backgroundColor = "#fdaaa7"; 
    });

    e.addEventListener("pointerleave", function(event) {
        var targetElement = event.target || event.srcElement;
        targetElement.style.backgroundColor = " #fff"; 
    });
});