package com.herts.orchestrator.service;

import com.herts.model.Context;

public interface AbstractService<I,O> {
    public O runProcess(Context ctx, I input);
}
