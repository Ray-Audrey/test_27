const gameBoard = document.getElementById("gameBoard");
const cards = document.querySelectorAll(".card");
const timerDisplay = document.getElementById("timer");
const movesDisplay = document.getElementById("moves");
const scoreDisplay = document.getElementById("score");
const startBtn = document.getElementById("startBtn");

const cardData = [
    {
        name: "Apple",
        emoji: "🍎",
        bg: "#ff4d4d"
    },
    {
        name: "Banana",
        emoji: "🍌",
        bg: "#ffd93d"
    },
    {
        name: "Grapes",
        emoji: "🍇",
        bg: "#8e44ad"
    },
    {
        name: "Orange",
        emoji: "🍊",
        bg: "#ff8c00"
    },
    {
        name: "Mango",
        emoji: "🥭",
        bg: "#ffb347"
    },
    {
        name: "Cherry",
        emoji: "🍒",
        bg: "#d90429"
    },
    {
        name: "Pear",
        emoji: "🍐",
        bg: "#8bc34a"
    },
    {
        name: "Watermelon",
        emoji: "🍉",
        bg: "#2ecc71"
    }
];

let gameCards = [];

let timer = 0;
let timerInterval = null;

let score = 0;
let moves = 0;
let matches = 0;

let firstCard = null;
let secondCard = null;

let firstIndex = -1;
let secondIndex = -1;

let temp = null;
let new_temp = null;

let lockBoard = true;

function shuffle(array){

    for(let i=array.length-1;i>0;i--){

        let j=Math.floor(Math.random()*(i+1));

        [array[i],array[j]]=[array[j],array[i]];

    }

}

function createCards(){

    gameCards=[...cardData,...cardData];

    shuffle(gameCards);

    cards.forEach((card,index)=>{

        card.classList.remove("flip");
        card.classList.remove("matched");

        card.dataset.index=index;

        const emoji=card.querySelector(".emoji");
        const name=card.querySelector(".name");
        const back=card.querySelector(".card-back");

        emoji.style.display="none";
        name.style.display="none";

        emoji.textContent=gameCards[index].emoji;
        name.textContent=gameCards[index].name;

        back.style.background=gameCards[index].bg;

    });

}

function resetValues(){

    timer=0;
    score=0;
    matches=0;
    moves=0;

    timerDisplay.textContent=timer;
    scoreDisplay.textContent=score;
    movesDisplay.textContent=moves;

    firstCard=null;
    secondCard=null;

    firstIndex=-1;
    secondIndex=-1;

    temp=null;
    new_temp=null;

}

function startTimer(){

    clearInterval(timerInterval);

    timerInterval=setInterval(()=>{

        timer++;

        timerDisplay.textContent=timer;

        if(timer>=180){

            clearInterval(timerInterval);

            lockBoard=true;

            alert(
                "Time Exceeded!\n\n"+
                "Time : "+timer+" seconds\n"+
                "Matched : "+matches+"/8\n"+
                "Score : "+score
            );

        }

    },1000);

}

startBtn.addEventListener("click",()=>{

    alert("Game Starts!");

    resetValues();

    createCards();

    startTimer();

    lockBoard=false;

});

cards.forEach(card=>{

    card.addEventListener("click",()=>{

        if(lockBoard) return;

        if(card.classList.contains("matched")) return;

        if(firstCard!==null && secondCard!==null) return;

        const index=parseInt(card.dataset.index);

        if(firstCard===null){

            firstCard=card;

            firstIndex=index;

            card.classList.add("flip");

            card.querySelector(".emoji").style.display="block";
            card.querySelector(".name").style.display="block";

            temp={
                emoji:gameCards[index].emoji,
                name:gameCards[index].name,
                bg:gameCards[index].bg
            };

            return;

        }

        secondCard=card;

        secondIndex=index;

        if(firstIndex===secondIndex){

            secondCard.classList.remove("flip");

            secondCard.querySelector(".emoji").style.display="none";
            secondCard.querySelector(".name").style.display="none";

            secondCard=null;
            secondIndex=-1;

            return;

        }

        secondCard.classList.add("flip");

        secondCard.querySelector(".emoji").style.display="block";
        secondCard.querySelector(".name").style.display="block";

        new_temp={

            emoji:gameCards[secondIndex].emoji,

            name:gameCards[secondIndex].name,

            bg:gameCards[secondIndex].bg

        };

        moves++;

        movesDisplay.textContent=moves;

        lockBoard=true;
        if(
            temp.emoji===new_temp.emoji &&
            temp.name===new_temp.name &&
            temp.bg===new_temp.bg
        ){

            firstCard.classList.add("matched");
            secondCard.classList.add("matched");

            score+=2;
            matches++;

            scoreDisplay.textContent=score;

            temp=null;
            new_temp=null;

            firstCard=null;
            secondCard=null;

            firstIndex=-1;
            secondIndex=-1;

            lockBoard=false;

            if(matches===8){

            clearInterval(timerInterval);

            setTimeout(()=>{

                alert(
                    "Congratulations!\n\n"+
                    "You Won!\n"+
                    "Time : "+timer+" seconds\n"+
                    "Matched : "+matches+"/8\n"+
                    "Score : "+score
                );

            },1000);

        }

        }
        else{

            setTimeout(()=>{

                firstCard.classList.remove("flip");
                secondCard.classList.remove("flip");

                firstCard.querySelector(".emoji").style.display="none";
                firstCard.querySelector(".name").style.display="none";

                secondCard.querySelector(".emoji").style.display="none";
                secondCard.querySelector(".name").style.display="none";

                firstCard=null;
                secondCard=null;

                firstIndex=-1;
                secondIndex=-1;

                temp=null;
                new_temp=null;

                lockBoard=false;

            },1000);

        }

    });

});

createCards();