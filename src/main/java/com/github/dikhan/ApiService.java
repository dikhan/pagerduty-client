package com.github.dikhan;

import com.github.dikhan.domain.EventResult;
import com.github.dikhan.domain.Incident;
import com.github.dikhan.exceptions.NotifyEventException;

public interface ApiService {

    EventResult notifyEvent(Incident incident) throws NotifyEventException;

}
