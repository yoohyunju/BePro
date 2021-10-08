package com.example.bepro.notice;

public class NoticeItems {
    private int noticeIdx; //공지 인덱스
    private String noticeCategory; //공지 종류
    private String noticeTitle; //공지 제목
    private String noticeContent; //공지 내용
    private String noticeDate; //공지 게시일

    @Override
    public String toString() {
        return "NoticeItems{" +
                "noticeIdx=" + noticeIdx +
                ", noticeCategory='" + noticeCategory + '\'' +
                ", noticeTitle='" + noticeTitle + '\'' +
                ", noticeContent='" + noticeContent + '\'' +
                ", noticeDate='" + noticeDate + '\'' +
                '}';
    }

    public NoticeItems(int noticeIdx, String noticeCategory, String noticeTitle, String noticeContent, String noticeDate) {
        this.noticeIdx = noticeIdx;
        this.noticeCategory = noticeCategory;
        this.noticeTitle = noticeTitle;
        this.noticeContent = noticeContent;
        this.noticeDate = noticeDate;
    }

    public int getNoticeIdx() {
        return noticeIdx;
    }

    public void setNoticeIdx(int noticeIdx) {
        this.noticeIdx = noticeIdx;
    }

    public String getNoticeCategory() {
        return noticeCategory;
    }

    public void setNoticeCategory(String noticeCategory) {
        this.noticeCategory = noticeCategory;
    }

    public String getNoticeTitle() {
        return noticeTitle;
    }

    public void setNoticeTitle(String noticeTitle) {
        this.noticeTitle = noticeTitle;
    }

    public String getNoticeContent() {
        return noticeContent;
    }

    public void setNoticeContent(String noticeContent) {
        this.noticeContent = noticeContent;
    }

    public String getNoticeDate() {
        return noticeDate;
    }

    public void setNoticeDate(String noticeDate) {
        this.noticeDate = noticeDate;
    }
}
