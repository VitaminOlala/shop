
	$(document).ready(function(){
		$("#logoutLink").on("click", function(e){
			e.preventDefault();
			document.logoutForm.submit();
		});
		
		customizeDropDownMenu();
	});

function customizeDropDownMenu(){
	//Khi cham vao profile se delay 1 khoang time va hien Log out
	$(" .navbar .dropdown").hover(
		function(){
			$(this).find('.dropdown-menu').first().stop(true, true).delay(250).slideDown();
		},
		function(){
			$(this).find('.dropdown-menu').first().stop(true, true).delay(100).slideUp();
		}
	);
	$(".dropdown > a").click(function(){
		location.href = this.href;
	});
}