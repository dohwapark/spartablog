let targetId;





$(document).ready(function () {
    const auth = getToken();

    if(auth !== '') {
        $('#login-true').show();
        $('#login-false').hide();
    } else {
        $('#login-false').show();
        $('#login-true').hide();
    }
    getMessages();
})

// 게시판 리스트 가져오기
function getMessages() {
    $('#posts-box').empty();
    $.ajax({
        type: "GET",
        url: "/posts",
        data: {},
        success: function (response) {
            for (let i = 0; i < response.length; i++) {
                let post = response[i];
                let id = post['id'];
                let username = post['username'];
                let title = post['title'];
                let modifiedAt = post['modifiedAt'];
                addHTML(id, username, title, modifiedAt);
            }
        }
    });
}

// 게시판 리스트 붙이기
function addHTML(id, username, title, modifiedAt) {
    let tempHtml = makeMessage(id, username, title, modifiedAt);
    $('#cards-box').append(tempHtml);
}

// 게시판 리스트 내용 만들기
function makeMessage(id, username, title, modifiedAt) {
    return `<tr id="${id}-tr" onclick="detailPost('${id}')" class="tr-hover">
                        <th scope="row">${id}</th>
                        <td>${username}</td>
                        <td>${title}</td>
                        <td>${modifiedAt}</td>
                    </tr>`;
}

// 게시글 자세히 보기 내용 가져오기
function detailPost(id) {
    $.ajax({
        type: "GET",
        url: `/posts/${id}`,
        data: {},
        success: function (response) {
            let post = response;
            let id = post['id'];
            let username = post['username'];
            let title = post['title'];
            let contents = post['contents'];
            let modifiedAt = post['modifiedAt'];
            addPostDetailHTML(id, username, title, contents, modifiedAt);
        }
    });
}

// 게시글 자세히 보기 닫기
function closeDetailPost(id) {
    $('.card').remove();
    $(`#${id}-tr`).removeAttr('onclick');
    $(`#${id}-tr`).attr('onclick', `detailPost('${id}')`);
}

// 게시글 자세히 보기 붙이기
function addPostDetailHTML(id, username, title, contents, modifiedAt) {
    let tempHtml = makePostDetail(id, username, title, contents, modifiedAt);
    $(`#${id}-tr`).after(tempHtml);
    $(`#${id}-tr`).removeAttr('onclick');
    $(`#${id}-tr`).attr('onclick', `closeDetailPost('${id}')`);
}

// 게시글 자세히 보기
function makePostDetail(id, username, title, contents, modifiedAt) {
    return `<td colspan="4">
                 <div class = "card">
                    <div class="card-header">
                        <div class="titledata">
                        제목 :
                            <span id="${id}-title" class="title">
                            ${title}
                            </span>
                        </div>
                    </div>
                    <div class = "card-body">
                        <div class="metadata">
                            <blockquote class="blockquote mb-0">
                                <div class="contents">

                                  <span id="${id}-contents" class="text"> ${contents} </span>
                                  <div id="${id}-editarea" class="edit">
                                        <textarea id="${id}-textarea" class="te-edit" name=""></textarea>
                                    </div>
                                </div>
                                   <footer class="blockquote-footer" style="margin-top: 10px">
                                   작성자 :
                                   <span id="${id}-username" class="username">${username} </span>
                                    </footer>
                                    <footer class="blockquote-footer">
                                    마지막 수정일 :
                                   <span class="metadata"> ${modifiedAt} </span>
                                    </footer>
                            </blockquote>
                        </div>
                    </div>
                        <div class="mybtns">
                            <input type="password" class="form-control post-detail-password" id="${id}-inputPassword" placeholder="비밀번호를 입력해주세요"
                                   name="password">
                            <button id="${id}-edit" class="icon-start-edit" onclick="editPost('${id}')" type="button" >수정 또는 삭제</button>
                            <button id="${id}-delete" class="icon-delete" onclick="deleteOne('${id}')" style="display: none" type="button" >삭제하기</button>
                            <button id="${id}-submit" class="icon-end-edit" onclick="submitEdit('${id}')" type="button" style="display: none" >저장하기</button>
                        </div>
                    </div>
                </td>`;
}

// 수정 또는 삭제하기 버튼
function editPost(id) {
    showEdits(id);
    let contents = $(`#${id}-contents`).text().trim();
    $(`#${id}-textarea`).val(contents);
}
// 수정하기
function showEdits(id) {
    $(`#${id}-editarea`).show();
    $(`#${id}-submit`).show();
    $(`#${id}-delete`).show();
    $(`#${id}-inputPassword`).show();

    $(`#${id}-contents`).hide();
    $(`#${id}-edit`).hide();
}

// 수정하기 취소
function hideEdits(id) {
    $(`#${id}-editarea`).hide();
    $(`#${id}-submit`).hide();
    $(`#${id}-delete`).hide();
    $(`#${id}-inputPassword`).hide();

    $(`#${id}-contents`).show();
    $(`#${id}-edit`).show();
}

// 수정내용 저장하기
function submitEdit(id) {
    let username = $(`#${id}-username`).text().trim();
    let contents = $(`#${id}-textarea`).val().trim();
    let password = $(`#${id}-inputPassword`).val().trim();
    if (isValidContents(contents) == false) {
        return;
    }
    let data = {'username': username, 'password': password, 'contents': contents};

    $.ajax({
        type: "PUT",
        url: `/posts/${id}`,
        contentType: "application/json",
        data: JSON.stringify(data),
        success: function (response) {
            if (response == 0) {
                alert('비밀번호가 일치하지 않습니다.');
                return;
            } else {
                alert('메시지 변경에 성공하였습니다.');
                window.location.reload();
            }
        }
    });
}


function isValidContents(contents) {
    if (contents == '') {
        alert('내용을 입력해주세요');
        return false;
    }
    if (contents.trim().length > 250) {
        alert('공백 포함 250자 이하로 입력해주세요');
        return false;
    }
    return true;
}

function writePost() {

    let contents = $('#contents').val();

    if (isValidContents(contents) == false) {
        return;
    }

    // const auth = getToken();
    let username = "${id}-username";
    let password = "${id}-username";
    let title = $('#inputTitle').val();
    let data = {'username': username, 'title': title, 'password': password, 'contents': contents};

    $.ajax({
        type: "POST",
        url: "/posts",
        contentType: "application/json",
        data: JSON.stringify(data),

        success: function (response) {
            alert('메시지가 성공적으로 작성되었습니다.');
            window.location.reload();
        }
    });
}

// 삭제하기
function deleteOne(id) {
    let password = $(`#${id}-inputPassword`).val().trim();
    let data = {'password': password};
    $.ajax({
        type: "DELETE",
        url: `/posts/${id}`,
        contentType: "application/json",
        data: JSON.stringify(data),
        success: function (response) {
            if (response == 0) {
                alert('비밀번호가 일치하지 않습니다.');
                return;
            } else {
                alert('메시지 삭제에 성공하였습니다.');
                window.location.reload();
            }
        }
    })
}

function open_box() {
    $('#post-box').show()
}

function close_box() {
    $('#post-box').hide()
}
function  getToken() {
    let cName = 'Authorization' + '=';
    let cookieData = document.cookie;
    let cookie = cookieData.indexOf('Authorization');
    let auth = '';
    if(cookie !== -1){
        cookie += cName.length;
        let end = cookieData.indexOf(';', cookie);
        if(end === -1)end = cookieData.length;
        auth = cookieData.substring(cookie, end);
    }
    return auth;
}
function logout() {
    // 토큰 값 ''으로 덮어쓰기
    document.cookie =
        'Authorization' + '=' + '' + ';path=/';
    window.location.reload();
}


