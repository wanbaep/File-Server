$(function() {
    console.log("hello");
    var $fileList = $(".file-list");
    var url = "/files";
    $.ajax(url).then(function(data) {
        console.log(data);
        for(var i = 0; i < data.length; i++) {
            var $div = $("<div />");
            var $spanId = $("<span />", {
                class: "col-sm-2 file-id",
                text: data[i].id
            });
            var $spanLength = $("<span />", {
                class: "col-sm-3",
                text: data[i].fileLength + " Bytes"
            });

            var $a = $("<a />", {
                class: "col-sm-3",
                href: "/files/" + data[i].id,
                text: data[i].fileName
            });
            
            var $date = $("<span />", {
                class: "col-sm-3",
                text: moment(data[i].createDate).format("YYYY.MM.DD")
            });
            
            var $update = $("<button />", {
                class: "col-sm-1 update",
                text: "수정",
                style: "width:10%; left:80%;"
            });

            var $delete = $("<button />", {
                class: "col-sm-1 delete",
                text: "삭제",
                style: "width:10%;left:80%"
            });
            var $wrapper = $("<div />", {
                class: "col-sm-1",
                style: "left:60%; width:15;"
            });
            var $input = $("<input />", {
                class: "update-file",
                type: "file",
                name: "updateFile"
            });
            $div.append("<br>").append($spanId).append($spanLength).append($a).append($date);
            
            if(data[i].contentType.split("/",1) == "image"){
                var $img = $("<img />", {
                    src: "/files/" + data[i].id,
                    alt: data[i].fileName,
                    height: 50,
                    width: 50,
                });
                $div.append($img);
            } else {
                var $replacement = $("<span />", {
                    text: "Not Image"
                });
                $div.append($replacement);
            }
            $wrapper.append($input);
            $div.append("<br>").append($wrapper).append($update).append($delete).append("<br>").append("<hr>");
            $fileList.append($div);
            
        }

        //delete
        $(".file-list").on("click", ".delete", function() {
            console.log("delete");
            var $parent =  $(this).closest("div");
            console.log($(this).closest("div").find(".file-id").text());
            var id = $parent.find(".file-id").text();
            var deleteUrl = "/files/" + id;

            $.ajax(deleteUrl, {
                method: "DELETE"
            }).then(function(data) {                
                $parent.remove();
            });
        });

        var updateFile = [];

        var $updateElement = $(".update-file");
        console.log($updateElement);

        $updateElement.change(function(e){
            var fileList = this.files;
            var index = $(this).closest("div").siblings(".file-id").text();
            updateFile[index] = fileList;

		});

        
        $(".file-list").on("click", ".update", function() {
            console.log("update");
            
            var $parent = $(this).closest("div");
            var id = $parent.find(".file-id").text();
            
            if(updateFile[id] === undefined) {
                
                return ;
            }

            var formData = new FormData();
            formData.append("update", true);
            formData.append("file",updateFile[id][0]);

            var updateUrl = "/files/" + id;
            
            console.log(updateFile[id]);

            console.log(formData);

            $.ajax("/files", {
                method: "POST",
                processData: false,
                contentType: false,
                data: formData
            }).then(function(data) {
                console.log("after POST %o",data);
                var bodyData = JSON.stringify({
                    "file":data
                });
                console.log(bodyData);
                $.ajax(updateUrl, {
                    method: "PUT",
                    contentType: "application/json",
                    data: JSON.stringify(data)
                }).then(function(res) {
                    console.log(res);
                    var temp = $parent.find(":first");
                    temp.next().next().next().text(res.fileLength);
                    $parent.find("a").text(res.fileName).next().text(res.modifyDate);
                    $parent.find("img").attr("alt",res.fileName);
                    
                    console.log($updateElement);
                    console.log(res.id);
                    
                    for(var i = 0; i < $updateElement.length; i++) {
                        $updateElement[i].value = "";
                    }
                });
            });
        });
    });
});