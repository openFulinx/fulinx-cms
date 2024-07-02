package com.fulinx.spring.data.mysql.enums;

import com.fulinx.spring.core.utils.MessageSourceUtils;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

public enum ArticleTypeEnum {

    BLOG(1, "enum.article.type.blog"),
    PAG(2, "enum.article.type.page"),
    PRODUCT(3, "enum.article.type.product"),
    VIDEO(4, "enum.article.type.video"),
    IMAGE(5, "enum.article.type.image");

    @Getter
    private final String messageKey;
    @Getter
    private final Integer index;
    @Getter
    private static final Map<Integer, ArticleTypeEnum> map = new LinkedHashMap<>();

    static {
        for (ArticleTypeEnum item : ArticleTypeEnum.values()) {
            map.put(item.getIndex(), item);
        }
    }

    ArticleTypeEnum(Integer index, String messageKey) {
        this.messageKey = messageKey;
        this.index = index;
    }

    public String getMessage() {
        return MessageSourceUtils.getMessage(messageKey);
    }

    public static Optional<ArticleTypeEnum> of(Integer index) {
        return Optional.ofNullable(map.get(index));
    }

    public static boolean contains(Integer index) {
        return map.get(index) != null;
    }

    public static Optional<String> getMessageByIndex(Integer index) {
        ArticleTypeEnum e = map.get(index);
        return e == null ? Optional.empty() : Optional.of(e.getMessage());
    }

    public static List<ArticleTypeEnum> getElementList() {
        return new ArrayList<>(map.values());
    }

    public static List<Map<String, Object>> getLanguageInfoList(String code) {
        return map.values().stream()
                .filter(languageEnum -> code == null || languageEnum.name().equalsIgnoreCase(code))
                .map(languageEnum -> {
                    Map<String, Object> info = new HashMap<>();
                    info.put("id", languageEnum.getIndex());
                    info.put("code", languageEnum.name());
                    info.put("articleTypeName", languageEnum.getMessage());
                    return info;
                })
                .collect(Collectors.toList());
    }

    public static List<Integer> getIndexList() {
        return map.values().stream().map(ArticleTypeEnum::getIndex).collect(Collectors.toList());
    }

    public static List<String> getMessageKeyList() {
        return map.values().stream().map(ArticleTypeEnum::getMessage).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return getMessage();
    }
}
