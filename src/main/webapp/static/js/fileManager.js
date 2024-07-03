const fileManager = {
    /**
    * 파일 업로드 처리
    *
    */
    upload(files, gid, location) {

        // FormData 생성자 - Content-Type: multipart/formData

        try {
            if (files.length == 0) {
                throw new alert("업로드할 파일을 선택하세요.");
            }

            if (!gid || !gid.trim()) {
                throw new alert("잘못된 접근입니다.");
            }

        } catch (err) {
            alert(err.message);
        }


    }
};