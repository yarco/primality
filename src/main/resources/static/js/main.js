$( document ).ready(function() {
	
	// SUBMIT FORM
    $("#customerForm").submit(function(event) {
		// Prevent the form from submitting via the browser.
		event.preventDefault();
		apiGet();
	});

	// Accepts only certain values
	(function($) {
		$.fn.inputFilter = function(inputFilter) {
			return this.on("input keydown keyup mousedown mouseup select contextmenu drop", function() {
				if (inputFilter(this.value)) {
					this.oldValue = this.value;
					this.oldSelectionStart = this.selectionStart;
					this.oldSelectionEnd = this.selectionEnd;
				} else if (this.hasOwnProperty("oldValue")) {
					this.value = this.oldValue;
					this.setSelectionRange(this.oldSelectionStart, this.oldSelectionEnd);
				}
			});
		};
	}(jQuery));

	$("#input").inputFilter(function(value) {
		return /^\d*$/.test(value); });

    
    function apiGet(){

		if(!/^\d+$/.test($('#input').val())){
			$("#getResultDiv").html("input error");
			return
		}
    	
    	// prep get values
    	var input = $("#input").val();
		resetData();
    	
    	// GET
    	$.ajax({
			type : "GET",
			url : window.location + "api/checkprime?input="+input,
			success : function(result) {
				if(result){
					finalText = "Error";
					notText = "";

					if(result.status === "false"){
						notText = " not"
					}
					// in case there's an error, return the result only in these cases
					if(result.status === "true" || result.status === "false"){
						finalText = result.input + " is" + notText + " prime";
					}

					$("#getResultDiv").html(finalText);
				}else{
					$("#getResultDiv").html("error");
				}
				console.log(result);

			},
			error : function(e) {
				$("#getResultDiv").html("input error");
				console.log("input error: ", e);
			}
		});
    }
    
    function resetData(){
    	$("#input").val("");
    }

});