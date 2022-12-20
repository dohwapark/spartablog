
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
        url: "/api/posts",
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
        url: `/api/posts/${id}`,
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
                            <span id="${id}-title" class="title">${title}</span>

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

                            <button id="${id}-edit" class="icon-start-edit" onclick="editPost('${id}', '${username}')" type="button" >수정 또는 삭제</button>
                            <button id="${id}-delete" class="icon-delete" onclick="deleteOne('${id}', '${username}')" style="display: none" type="button" >삭제하기</button>
                            <button id="${id}-submit" class="icon-end-edit" onclick="submitEdit('${id}', '${username}')" type="button" style="display: none" >저장하기</button>
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
    const auth = getToken();
    let contents = $(`#${id}-textarea`).val().trim();


    if (isValidContents(contents) == false) {
        return;
    }
    let data = {'contents': contents};

    $.ajax({
        type: "PUT",
        url: `/api/posts/${id}`,
        contentType: "application/json",
        data: JSON.stringify(data),
        beforeSend: function (xhr) {
            xhr.setRequestHeader("Authorization", auth);
        },
        success: function (response) {
            if(response == 0){
                alert('본인의 게시물만 수정 또는 삭제가 가능합니다.');
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

// function writePost() {
//
//     let contents = $('#contents').val();
//     //
//     if (isValidContents(contents) == false) {
//         return;
//     }
//
//     // const auth = getToken();
//     // let username = [[${username}]]
//     let title = $('#inputTitle').val();
//     let data = { 'title': title, 'contents': contents};
//
//     $.ajax({
//         type: "POST",
//         url: "/api/posts",
//         contentType: "application/json",
//         data: JSON.stringify(data),
//
//         success: function (response) {
//             alert('메시지가 성공적으로 작성되었습니다.');
//             window.location.reload();
//         }
//     });
// }


function writePost() {
    /**
     * modal 뜨게 하는 법: $('#container').addClass('active');
     * data를 ajax로 전달할 때는 두 가지가 매우 중요
     * 1. contentType: "application/json",
     * 2. data: JSON.stringify(itemDto),
     */
    let contents = $('#contents').val();
    if (isValidContents(contents) == false) {
        return;
    }

    const auth = getToken();


    let title = $('#inputTitle').val();
    let data = {'title': title, 'contents': contents};

    // 1. POST /api/memos 에 메모 생성 요청
    $.ajax({
        type: "POST",
        url: `/api/posts`,
        contentType: "application/json",
        data: JSON.stringify(data),
        beforeSend: function (xhr) {
            xhr.setRequestHeader("Authorization", auth);
        },
        success: function (response) {
            // 2. 응답 함수에서 modal을 뜨게 하고, targetId 를 reponse.id 로 설정


            alert('메시지가 성공적으로 작성되었습니다.');
            window.location.reload();
        }
    })
}


// 삭제하기
// function deleteOne(id) {
//     const auth = getToken();
//     let password = $(`#${id}-inputPassword`).val().trim();
//     let data = {'password': password};
//     $.ajax({
//         type: "DELETE",
//         url: `/api/posts/${id}`,
//         contentType: "application/json",
//         data: JSON.stringify(data),
//         success: function (response) {
//             if (response == 0) {
//                 alert('비밀번호가 일치하지 않습니다.');
//                 return;
//             } else {
//                 alert('메시지 삭제에 성공하였습니다.');
//                 window.location.reload();
//             }
//         }
//     })
// }


function deleteOne(id) {

    // let loginUser = [[${username}]];
    // if (loginUser !== username) {
    //     alert("글쓴이만 삭제 가능합니다.");
    //     return;
    // }
    const auth = getToken();
    // let loginUser = $(`#${id}-username`).val().trim();
    // let data = {'username': loginUser};

    $.ajax({
            type: "DELETE",
            url: `/api/posts/${id}`,
            contentType: "application/json",
            // data: JSON.stringify(data),

            beforeSend: function (xhr) {
                xhr.setRequestHeader("Authorization", auth);
            },
            success: function (response) {
                if(response == 0){
                    alert('본인의 게시물만 수정 또는 삭제가 가능합니다.');
                    return;
                } else {
                    alert('메시지 삭제에 성공하였습니다.');
                    window.location.reload();
                }

            }
        }
    )
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


