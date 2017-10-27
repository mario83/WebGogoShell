<%@ include file="./init.jsp" %>


<div class="panel" tabindex="0" autofocus>
	<div class="panel-body">
		<pre id="gogo-container"></pre><span class="blink_me hidden">&nbsp;</span>
	</div>
	<div class="panel-footer">
		<aui:button-row>
			<aui:button value="gogo-clear" onClick="$('#gogo-container').html('');setFocus();"/>
			<aui:button value="gogo-copy" onClick="copyToClipboard();setFocus();"/>
			<aui:button value="gogo-copy-selected" onClick="copySelectedToClipboard();setFocus();"/>		
		</aui:button-row>
	</div>

</div>

<aui:script>
	(function (A) {
		var handle = Liferay.on('destroyPortlet', function (event) {
			if (event.portletId === '<%= portletDisplay.getRootPortletId() %>') {
				websocketModule.websocketClose();
				handle.detach();
			}
		});
	}(AUI()));
</aui:script>

<script type="text/javascript">

	$(window).on("load", function() {
		setFocus();
	});

	function setFocus(){
		$("[autofocus]").focus();
	}

	$(function() {
		$("[autofocus]").focusout(function() {
			$(".blink_me" ).addClass("hidden");
		}).focusin(function() {
			$(".blink_me" ).removeClass("hidden");
		});
		$("[autofocus]").on("keyup",keyUpEvent).on("keydown",keyDownEvent).bind("paste", pastEvent);
		
		setFocus();
	});
</script>
