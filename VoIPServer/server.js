var express = require("express");
var app = express();
var server = require("http").createServer(app);
var io = require("socket.io").listen(server.listen(process.env.PORT || 5000));
//var io = require("socket.io").listen(server.listen(5000 , '', () => {
	//console.log(`Server running `);
//}));
var users=[];
app.get("/", function(req, res){
    	res.send('Server is running 5000');	
});

// Super simple server:
//  * One room only. 
//  * We expect two people max. 
//  * No error handling.

io.sockets.on('connection', function (client) {
	
	client.on('join' , function(data){
		if(!users.includes(data.myId)){
			users.push(data.myId);
		// }else{
			console.log('User ' + data.myId + ' existed');
		}
		client.join(data.myId);
		console.log('User ' + data.myId + ' has connected');
	});

	client.on('leave', function(data){
		console.log('User ' + data.myId + ' has left');
		client.leave(data.myId);
		users.pop(data.myId);
	});
	
	client.on('offer', function (data) {
		io.sockets.in(data.hisId).emit('offer' , data);
        console.log('offer: ' + JSON.stringify(data));
    });

	 client.on('accept', function (data) {
		io.sockets.in(data.hisId).emit('accept' , data);
        console.log('accept: ' + JSON.stringify(data));
    });
	
	 client.on('decline', function (data) {
		io.sockets.in(data.hisId).emit('decline' , data);
        console.log('decline: ' + JSON.stringify(data));
    });

    client.on('candidate', function (data) {
		io.sockets.in(data.hisId).emit('candidate' , data);
        console.log('candidate: ' + JSON.stringify(data));
    });
	
	 client.on('finish', function (data) {
		io.sockets.in(data.hisId).emit('finish' , data);
        console.log('finish: ' + JSON.stringify(data));
    });
	
	 client.on('type', function (data) {
		io.sockets.in(data.hisId).emit('type' , data);
        console.log('type: ' + JSON.stringify(data));
    });

    // Here starts evertyhing!
    // The first connection doesn't send anything (no other clients)
    // Second connection emits the message to start the SDP negotation
 
});
