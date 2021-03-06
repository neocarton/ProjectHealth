package me.lam.huyen.channel;

import me.lam.huyen.client.GitHubClient;
import me.lam.huyen.model.Data;
import me.lam.huyen.model.GitIssue;
import me.lam.huyen.model.GitTopIssues;
import me.lam.huyen.service.DataService;
import me.lam.huyen.service.StateService;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IssueLoader {

	private Logger logger = LoggerFactory.getLogger(IssueLoader.class);

	@Value("${app.loader.issue.project_count}")
	private int projectCount;

	@Value("${app.loader.issue.page_size}")
	private int pageSize;

	@Value("${app.loader.issue.max_result}")
	private int maxResult;

	@Autowired
	private GitHubClient gitHubClient;

	@Autowired
	private DataService dataService;

	@Autowired
	private StateService stateService;

	/**
	 * Handle event git projects loaded.
	 */
	@Scheduled(initialDelayString = "${app.loader.issue.delay.inMilliSecond}",
			fixedDelayString = "${app.loader.issue.delay.inMilliSecond}")
	public void fetchIssues() {
		List<Data> projects = dataService.findProjectsHaveNoIssue(projectCount);
		if (projects == null || projects.isEmpty()) {
			return;
		}
		logger.debug("Fetching issues for projects: {}",
				projects.stream().map(Data::getValue).collect(Collectors.toList()));
		for (Data project : projects) {
			fetchIssuesAndSave(project);
		}
	}

	private void fetchIssuesAndSave(Data project) {
		String id = project.getObjectId();
		String repos = project.getValue();
		try {
			int page = 1;
			List<GitIssue> issues = new ArrayList<>(maxResult);
			while (issues.size() < maxResult) {
				// Load commit statistics
				List<GitIssue> curIssues = gitHubClient.getIssues(repos, "all", "created", "desc", page++, pageSize);
				if (curIssues == null || curIssues.isEmpty()) {
					break;
				}
				// Add to result lists
				for (GitIssue issue : curIssues) {
					if (issue.getPullRequest() == null) {
						issues.add(issue);
					}
				}
			}
			// Save data and state
			GitTopIssues<GitIssue> topIssues = new GitTopIssues<>(issues);
			dataService.saveIssues(id, topIssues);
		}
		catch (Exception exc) {
			logger.warn("Failed to fetch issues for project '{}': {}", repos,
					ExceptionUtils.getRootCauseMessage(exc));
			logger.trace("Exception", exc);
			dataService.saveIssuesError(id, exc);
		}
	}
}
