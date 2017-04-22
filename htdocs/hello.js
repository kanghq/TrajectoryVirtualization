$(document).ready(function() {
    $.ajax({
        url: "http://api2.hqkang.com:8080/Trajectories/"
    }).then(function(data) {
    
    	 

        
    	
    	var content='';
    	$.each(data,function(i,item ) {
    	
    	 
    	content += '<input type="checkbox" name="TraID" id="'+item+'"/>'+item+"<br>";

       //$('.greeting-id').append(item+"<br>");
       $('.greeting-content').append(item.content);
       });
       content += "</form>"
        $('#executerDiv').append(content);
    });
});

