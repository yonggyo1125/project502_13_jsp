const fileManager = {
    /**
    * 파일 업로드 처리
    *
    */
    upload(files, gid, location) {
        console.log(files, gid, location);
        // FormData 생성자 - Content-Type: multipart/formData
        try {
            if (files.length == 0) {
                throw new alert("업로드할 파일을 선택하세요.");
            }

            if (!gid || !gid.trim()) {
                throw new alert("잘못된 접근입니다.");
            }

            const formData = new FormData();
            formData.append("gid", ("" + gid).trim());

            if (location && location.trim()) {
                formData.append("location", location);
            }

            for(const file of files) {
                formData.append("file", file);
            }

            const rootUrl = document.querySelector("meta[name='rootUrl']").content;
            const apiUrl = `${rootUrl}file/upload`;

            fetch(apiUrl, {
                method: 'POST',
                cache: 'no-cache',
                body: formData
            })
            .then(res => res.json())
            .then(json => console.log(json));

        } catch (err) {
            alert(err.message);
        }


    }
};