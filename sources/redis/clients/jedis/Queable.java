package redis.clients.jedis;

import java.util.LinkedList;
import java.util.Queue;

/* loaded from: jedis-2.9.3.jar:redis/clients/jedis/Queable.class */
public class Queable {
    private Queue<Response<?>> pipelinedResponses = new LinkedList();

    protected void clean() {
        this.pipelinedResponses.clear();
    }

    protected Response<?> generateResponse(Object data) {
        Response<?> response = this.pipelinedResponses.poll();
        if (response != null) {
            response.set(data);
        }
        return response;
    }

    protected <T> Response<T> getResponse(Builder<T> builder) {
        Response<T> lr = new Response<>(builder);
        this.pipelinedResponses.add(lr);
        return lr;
    }

    protected boolean hasPipelinedResponse() {
        return !this.pipelinedResponses.isEmpty();
    }

    protected int getPipelinedResponseLength() {
        return this.pipelinedResponses.size();
    }
}
