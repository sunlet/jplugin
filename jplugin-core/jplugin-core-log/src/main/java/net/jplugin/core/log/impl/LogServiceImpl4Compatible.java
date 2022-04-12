package net.jplugin.core.log.impl;

import net.jplugin.core.log.api.ILogService;
import net.jplugin.core.log.api.LogFactory;
import net.jplugin.core.log.api.Logger;

public class LogServiceImpl4Compatible implements ILogService {

	public Logger getLogger(String name) {
		return LogFactory.getLogger(name);
	}

	public Logger getSpecicalLogger(String filename) {
		return LogFactory.getSpecicalLogger(filename);
	}

}
