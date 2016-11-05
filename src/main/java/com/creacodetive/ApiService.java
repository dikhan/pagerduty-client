package com.creacodetive;

import com.creacodetive.domain.EventResult;
import com.creacodetive.domain.Incident;
import com.creacodetive.exceptions.NotifyEventException;

public interface ApiService {

    EventResult notifyEvent(Incident incident) throws NotifyEventException;

}
