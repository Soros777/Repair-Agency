jQuery(document).ready(function( $ ) {

	if($('#canvas').length) {

		var doughnutData = [{
        value: 70,
        color: "#f85c37"
      },
      {
        value: 30,
        color: "#ecf0f1"
      }
    ];
    var myDoughnut = new Chart(document.getElementById("canvas").getContext("2d")).Doughnut(doughnutData);
	};

	if($('#canvas2').length) {
		var doughnutData = [{
				value: 90,
				color: "#f85c37"
			},
			{
				value: 10,
				color: "#ecf0f1"
			}
		];
		var myDoughnut = new Chart(document.getElementById("canvas2").getContext("2d")).Doughnut(doughnutData);
	}

	if($('#canvas3').length) {
		var doughnutData = [{
				value: 55,
				color: "#f85c37"
			},
			{
				value: 45,
				color: "#ecf0f1"
			}
		];
		var myDoughnut = new Chart(document.getElementById("canvas3").getContext("2d")).Doughnut(doughnutData);
	}

	if($('#canvas4').length) {
		var doughnutData = [{
				value: 55,
				color: "#f85c37"
			},
			{
				value: 45,
				color: "#ecf0f1"
			}
		];
		var myDoughnut = new Chart(document.getElementById("canvas4").getContext("2d")).Doughnut(doughnutData);
	}
});

$('.login-btn').click(function (e) {

	e.preventDefault();

	let email = $('input[name="email"]').val();
	let password = $('input[name="password"]').val();

	$.ajax({
		url: 'controller',
		type: 'POST',
		dataType: 'text',
		data: {
			email: email,
			password: password,
			command: 'login'
		},
		success (data) {

			let welcomeHtml = "<h1>" +
				"Добро пожаловать, " + data + "!" +
				"</h1>" +
				"<a class='btn btn-success btn-lg m-2' href='controller'>На сайт</a>" +
				"<a class='btn btn-primary btn-lg m-2' href='controller?p=clientMain'>В личный кабинет</a>";
			let userField = "<a href='controller?p=clientMain' class='userName'>" + data + "</a>";

			if(data === "Something wrong") {
				$('.msg-fal-log').removeClass('none');
				$('.form-pop-link').html("Register");
			} else {
				$('.success-message').html(welcomeHtml);
				$('.userField').html(userField);
			}
		}
	});
});

function goBack() {
	$('.msg-fal-log').addClass('none');
	$('.form-pop-link').html(" or Register");
	clearForm();
}

function roll() {
	let roll = $('.msg-fal-log');
	if(!roll.hasClass('none')) {
		roll.addClass('none');
		$('.form-pop-link').html(" or Register");
		clearForm();
	}
}

function clearForm() {
	let inputF = document.querySelector('.log');
	let passF = document.querySelector('.pass');
	inputF.value = '';
	passF.value = '';
}
