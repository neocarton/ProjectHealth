package me.lam.huyen.channel;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface GitProjectChannel {

	String PROJECT_LOADED = "projectLoaded";

	String COMMIT_STAT_LOADED = "commitStatLoaded";

	String ISSUE_LOADED = "issueLoaded";

	@Output
	MessageChannel projectLoaded();

	@Output
	MessageChannel commitStatLoaded();

	@Output
	MessageChannel issueLoaded();
}
