function loadProfile(){
	// 첫번째 0은 $('#profile') 객체의 0이라는 이름을 가진 필드
	// 두번째 0은 <input type='file> 이 가지는 files라는 배열속성의 0번째 원소
	const file=$('#profile')[0].files[0];
	const maxSize=1024*1024;
	
	if(file.size>maxSize){
		alert("용량을 초과하였습니다. 용량을줄여주세요");
		$('#profile').val("");
		return false;
	}
	
	let reader = new FileReader();
	/*
	이미지를 읽어서 문자열 형식을 변환
	문자열로 각종 데이터를 표현하는 인코딩 형식: base64
	readAsDataURL로 이미지를 base64로 변환해서 실행하는 이벤트 핸들러
	*/	
	reader.readAsDataURL(file);
	
	reader.onload= function(e){
		$('#show-profile').attr("src",e.target.result);
	}
	return true;
}

// 체크하고 확인할것들
	// username, password (1,2), email, 생년월일 체크
	//확인할 파라미터 : 입력값,에러메시지 출력할곳, 패턴 , empty 메시지, 패턴체크 실패시 메시지
	//               출력값, $출력한값, 
function emptyAndpatternCheck(value, error , pattern , emptyerror, patternerrormsg ){
	error.text('');
	if(value==''){
		error.text(emptyerror);
		return false;
	}else if(pattern.test(value)==false){
		error.text(patternerrormsg);
		return false;
	}
	return true;
}

function birthdayCheck() {
	const pattern = /^[0-9]{4}-[0-9]{2}-[0-9]{2}$/
	const value = $('#birthday').val();
	const $birthdayMsg = $('#birthday-msg');
	return emptyAndpatternCheck(value, $birthdayMsg, pattern, '생일을 입력하세요', '정확한 생일을 입력하세요');
}

function emailCheck() {
	const pattern = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;
	const value = $('#email').val();
	const $emailMsg = $('#email-msg');
	return emptyAndpatternCheck(value, $emailMsg, pattern, '이메일 형식인지 확인해주세요', '정확한 이메일 형식이 아닙니다');
}

function passwordCheck(id) {
	const pattern = /^[A-Za-z0-9]{10,20}$/;
	const value = $('#'+id).val();
	const $passwordMsg = $('#'+id+'-msg');
	return emptyAndpatternCheck(value, $passwordMsg, pattern, '비밀번호를 입력하세요', '비밀번호는 영숫자 10~20자입니다');
}

function password2Check() {
	const password = $('#new-password').val();
	const password2 = $('#password2').val();
	const $password2Msg = $('#password2-msg');
	$password2Msg.text('');
	if(password==''){
		$password2Msg.text("비밀번호를 다시 입력하세요");
		return false;
		}else if(password!=password2){
			$password2Msg.text("위의 비밀번호와 일치하지 않습니다.");
			return false;
		}
		return true;
}
// availableCheck = 값을 입력하고 사용가능한지 불가능한지 판단하기위해 사용 
async function usernameCheck(availableCheck=true){
	const pattern = /^[A-Za-z0-9]{6,10}$/;
	const value = $('#username').val();
	const $usernameMsg = $('#username-msg');
	const result = emptyAndpatternCheck(value, $usernameMsg, pattern, '아이디를 입력하세요', '아이디는 영숫자 6~10자입니다');
	if(result==false)
		return false;
	if(availableCheck==false)
		return result;
	//사용가능한지 판단
	try{
		await $.ajax("/member/username/available?username="+value);
		return true;
		}catch(err){
			console.log(err);
			$usernameMsg.text("중복된 아이디입니다.")
			return false;
	}
}
	

