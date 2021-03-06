package me.lam.huyen.model;

import java.util.Collections;
import java.util.List;

public class GitCommitStatWeekly {

	private List<GitCommitStat> items = Collections.emptyList();

	private int dayCount = 0;

	private int weekCount = 0;

	private int totalCommit = 0;

	public GitCommitStatWeekly() {
	}

	public GitCommitStatWeekly(List<GitCommitStat> items) {
		this.items = items;
		init();
	}

	private void init() {
		if (items == null || items.isEmpty()) {
			return;
		}
		weekCount = items.size();
		dayCount = weekCount * 7;
		for (GitCommitStat item : items) {
			weekCount++;
			dayCount += item.getDays().size();
			totalCommit += item.getTotal();
		}
	}

	public List<GitCommitStat> getItems() {
		return items;
	}

	public void setItems(List<GitCommitStat> items) {
		this.items = items;
	}

	public int getDayCount() {
		return dayCount;
	}

	public void setDayCount(int dayCount) {
		this.dayCount = dayCount;
	}

	public int getWeekCount() {
		return weekCount;
	}

	public void setWeekCount(int weekCount) {
		this.weekCount = weekCount;
	}

	public int getTotalCommit() {
		return totalCommit;
	}

	public void setTotalCommit(int totalCommit) {
		this.totalCommit = totalCommit;
	}

	public Float getDailyAverage() {
		return (dayCount != 0) ? ((float) totalCommit / dayCount) : null;
	}

	public Float getWeeklyAverage() {
		return (weekCount != 0) ? ((float) totalCommit / weekCount) : null;
	}

	@Override
	public String toString() {
		return "GitCommitStatWeekly{" +
				"items=" + items +
				", dayCount=" + dayCount +
				", weekCount=" + weekCount +
				", totalCommit=" + totalCommit +
				", dailyAverage=" + getDailyAverage() +
				", weeklyAverage=" + getWeeklyAverage() +
				'}';
	}
}
