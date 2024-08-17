package top.cary61.carycode.commons.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PollingMessage <T> {

    private String uuid;

    private boolean ready;

    private long timeIntervalMillis;

    private T data;
}
