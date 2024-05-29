package Opensource_SW_Project.Project.web.dto.Diary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class DiaryResponseDTO{

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateDiaryResultDTO {
        Long userId;
        Long diaryId;
        String content;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DiaryDTO {
        //Long userId;
        Long diaryId;
        String content;
    }


    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DiaryPreviewDTO {    // 특정 일기 조회
        Long userId;
        Long diaryId;
        String content;

    }


    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateDiaryResultDTO {
        Long userId;
        Long diaryId;
        String content;
    }
}