package org.mirrentools.ost.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import org.mirrentools.ost.common.EventBusAddress;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class MetricsVerticle extends AbstractVerticle {
    private static long totalSendCount = 0;
    private static long startTime = 0;
    private static long endTime = 0;
    private static long successCount = 0;
    private static long failCount = 0;
    private static long totalDelay = 0;
    private static int minDelay = -1;
    private static int maxDelay = 0;

    private long lastMetricsTime = 0;
    private long lastMetricsCount = 0;

    static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    StringBuilder sb = new StringBuilder(1024);
    long timerId = 0;

    @Override
    public void start() throws Exception {

        try {
            startTime = System.currentTimeMillis();
            totalSendCount = config().getLong("sendCount");
            vertx.eventBus().consumer(EventBusAddress.TEST_METRICS_HANDLER, this::metrics);

            timerId = vertx.setPeriodic(5000, tid -> {
                showResult(System.currentTimeMillis());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void showResult(long now) {
        sb.setLength(0);

        long endSum = (successCount + failCount);

        Instant instant = Instant.ofEpochMilli(now);
        sb.append("\n");
        sb.append(dateTimeFormatter.format(LocalDateTime.ofInstant(instant, ZoneId.systemDefault())));

        sb.append("\t 距开始时间:").append(String.format("%.2f", ((double) now - startTime) / 1000)).append("s");
        if (lastMetricsTime > 0) {
            sb.append("\t 距上次统计时间:").append(String.format("%.2f", ((double) now - lastMetricsTime) / 1000)).append("s\n");
        } else {
            sb.append("\n");
            lastMetricsTime = startTime;
        }

        sb.append("当前统计区间: ").append(endSum - lastMetricsCount).append("\t 累计执行: ").append(endSum).append("\t 累计成功: ").append(successCount).append("\t 累计失败: ").append(failCount).append(" \n");
        if ((endSum - lastMetricsCount) > 0) {
            sb.append("当前统计区间tps: ").append(String.format("%.2f", ((double) endSum - (double) lastMetricsCount) / ((now - lastMetricsTime)) * 1000)).append("/s\t总TPS:").append(String.format("%.2f", ((double) endSum) / ((now - startTime)) * 1000)).append("/s\n");
            sb.append("累计耗时: ").append(totalDelay).append("ms\t 平均耗时: ").append(String.format("%.2f", (double) totalDelay / (double) endSum)).append("ms\t 最大耗时: ").append(maxDelay).append("ms\t 最小耗时: ").append(minDelay).append("ms");
        }
        lastMetricsCount = endSum;
        lastMetricsTime = now;

        System.out.println(sb);
        if (endTime > 0) {
            System.out.println("测试完成,开始时间:" + dateTimeFormatter.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(startTime), ZoneId.systemDefault())) + ", 结束时间:" + dateTimeFormatter.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(endTime), ZoneId.systemDefault())) + ", 用时:" + ((now - startTime) / 1000) + "秒");
            this.getVertx().close();
            System.exit(0);

        }
    }

    private void metrics(Message<JsonObject> msg) {
        boolean status = msg.body().getBoolean("status");
        if (status) {
            successCount++;
        } else {
            failCount++;
        }

        int delay = msg.body().getInteger("delay");
        totalDelay += delay;
        if (minDelay == -1) {
            minDelay = delay;
            maxDelay = delay;
            return;
        }
        if (delay > maxDelay) {
            maxDelay = delay;
        } else if (delay < minDelay) {
            minDelay = delay;
        }
        if (successCount + failCount >= totalSendCount) {
            endTime = System.currentTimeMillis();
            vertx.cancelTimer(timerId);
            showResult(endTime);
        }
    }


}
