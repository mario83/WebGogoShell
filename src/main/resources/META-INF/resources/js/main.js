var websocketModule = (function () {

	var wsUri = 'ws://' +window.location.host+ '/o/websocket/gogoshell';
	if(window.location.protocol == "https:"){
		wsUri = 'wss://' +window.location.host+ '/o/websocket/gogoshell';
	}

	var websocket = new WebSocket(wsUri);

	websocket.onmessage = function(event) {
		var container = $('#gogo-container');
		if(event.data.startsWith(String.fromCharCode(0x08))){
			container.html(container.html().substr(0,container.html().length-1));
		} else {
			container.append(event.data);
		}
		$(".web-gogo-shell-portlet .panel-body").scrollTop($(".web-gogo-shell-portlet .panel-body").prop('scrollHeight'));
	};

	websocket.onclose = function(event) {
	  $('#gogo-container').append('<div>websocket closed</div>');
	  $(".web-gogo-shell-portlet .panel-body").scrollTop($(".web-gogo-shell-portlet .panel-body").prop('scrollHeight'));
	};

	return {
		websocketSend: function(text) {
			websocket.send(text);
		},
		websocketClose: function() {
			websocket.close();
		}
	}
})();

function getSelectedText(el) {
    if (typeof el.selectionStart == "number") {
        return el.value.slice(el.selectionStart, el.selectionEnd);
    } else if (typeof document.selection != "undefined") {
        var range = document.selection.createRange();
        if (range.parentElement() == el) {
            return range.text;
        }
    }
    return "";
}

function copySelectedToClipboard() {
	var $temp = $("<textarea>");
	$("body").append($temp);
  $temp.val(getSelectedText($("#gogo-container")));
  document.execCommand("copy");
  $temp.remove();
}

function copyToClipboard() {
  var $temp = $("<textarea>");
  $("body").append($temp);
  $temp.val($("#gogo-container").text()).select();
  document.execCommand("copy");
  $temp.remove();
}

function sendCommand(event){
	var keyCode = event.which || event.keyCode;
	if(keyCode == 13){
		websocketModule.websocketSend('\r\n');
	}
	if(event.key.length == 1) {
		websocketModule.websocketSend(event.key);
		event.target.value = "";
		console.log("post "+event.key);
	}
}

function sendAutocomplete(event){
	var keyCode = event.which || event.keyCode;
	if(keyCode == 9){
		event.preventDefault();
		event.stopPropagation();
		websocketModule.websocketSend('\t');
	}
	if(keyCode == 8){
		event.preventDefault();
		event.stopPropagation();
		websocketModule.websocketSend(String.fromCharCode(0x08)+String.fromCharCode(0x7f));
	}
	if(keyCode == 32 ||
		keyCode == 37 ||
		keyCode == 38 ||
		keyCode == 39 ||
		keyCode == 40){
			event.preventDefault();
			event.stopPropagation();
	}

}
