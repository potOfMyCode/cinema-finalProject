function notify_tick(movie, day, date, time, place){
    document.getElementById("mov").innerText = movie.toString();
    document.getElementById("day").innerText = day.toString();
    document.getElementById("date").innerText = (date.getMonth() + 1) + "-" + date.getDate() + "-" + date.getFullYear();
       // document.getElementById("date").innerText = (date.getDate() + "-" + date.getMonth() + 1)  + "-" + date.getFullYear();
    document.getElementById("time").innerText = time;
    document.getElementById("place").innerText = place;
    appearDivById("order-form");
}

function notify_cancel(ticket) {
    document.getElementById("tk-num-holder").innerText = ticket;
}

function notify_del(msg, holderId) {
    document.getElementById(holderId.toString()).innerText = msg;
}

function cls_span(id) {
    $('#' + id.toString()).slideUp(200);
}

function appearDivById(id) {
    if(document.getElementById(id).style.display === 'none'){
        $('#' + id.toString()).fadeTo(800, 30);
    }
}