package com.example.ordermanagement.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    MEAL_NOT_FOUND("部分餐品已下架，请刷新重新获取餐品"),
    SERVER_UNAVAILABLE("下单失败，当前无法获取餐品信息，请稍后重试");
    private final String message;
}
