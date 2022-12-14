package com.sparta.spartablog.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass // 상속했을 때, 컬럼으로 인식
@EntityListeners(AuditingEntityListener.class) // 생성, 수정시가능을 자동으로 반영
public class Timestamped {
    @LastModifiedDate // 마지막 수정일자임을 나타냅니다.
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifiedAt;

    @CreatedDate // 생성일자임을 나타냅니다.
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;        // LocalDateTime은 시간을 나타내는 자료형
}

