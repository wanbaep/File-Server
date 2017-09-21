# File-Server

- 파일 서비스를 제공하는 REST API

### 기능
- 파일 조회/추가/수정/삭제 기능
- [x] GET /files - list all files
- [x] POST /files - upload a new file
- [x] GET /files/<id> - download the file identified by id
- [x] PUT /files/<id> - update the existing file identified by id
- [x] DELETE /files/<id> - delete the existing file identified by id

### application.properties
```
application.database.driver-class-name="DATABASE DRIVER NAME"
application.database.url="DATABASE URL"
application.database.username="DATABASE USER"
application.database.password="DATABASE PASS"
application.file.basedir="IMAGE UPLOAD BASE DIRECTORY"
application.file.maximum="MAXIMUM FILE SIZE TO BE UPLOAD"
```
