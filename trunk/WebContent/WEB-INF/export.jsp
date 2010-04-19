<%@ page contentType="text/html; charset=UTF-8" session="false"%><%@
 taglib uri="/struts-tags" prefix="s"%><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "html-dtd/xhtml1-transitional.dtd">
<html>
<head>
<title>Data Export</title>
<link rel="stylesheet" type="text/css" href="backend-style.css" />
</head>

<body>

<h1>Players</h1>

<table border="1">
	<tr>
		<th>ID</th>
		<th>Name</th>
		<th>Sex</th>
		<th>DoB</th>
		<th>Place</th>
	</tr>
	<s:iterator value="players">
		<tr id="player-<s:property value="id" />">
			<td><s:property value="id" /></td>
			<td><s:property value="name" /></td>
			<td><s:property value="sex" /></td>
			<td><s:date name="dateOfBirth" /></td>
			<td><s:property value="place" /></td>
		</tr>
	</s:iterator>
</table>

<h1>Scenarios</h1>

<table border="1">
	<tr>
		<th>ID</th>
		<th>Type</th>
		<th>Rounds</th>
	</tr>
	<s:iterator value="scenarios">
		<tr id="scenario-<s:property value="id" />">
			<td><s:property value="id" /></td>
			<td><s:property value="type" /></td>
			<td>
			<table border="1">
				<tr>
					<th rowspan="2">ID</th>
					<th rowspan="2">Position</th>
					<th rowspan="2">Baseline Modifier</th>
					<th rowspan="2">Expected Playtime</th>
					<th colspan="3">Difficulty</th>
					<th rowspan="2">Baseline</th>
				</tr>
				<tr>
					<th>interval</th>
					<th>speed</th>
					<th>ratio</th>
				</tr>
				<s:iterator value="rounds">
					<tr id="scenarioround-<s:property value="id" />">
						<td><s:property value="id" /></td>
						<td><s:property value="position" /></td>
						<td><s:property value="baselineModifier" /></td>
						<td><s:property value="expectedPlaytime" /></td>
						<td><s:property value="difficultyFunction.interval" /></td>
						<td><s:property value="difficultyFunction.speed" /></td>
						<td><s:property value="difficultyFunction.ratio" /></td>
						<td><s:property value="baselineRound" /></td>
					</tr>
				</s:iterator>
			</table>
			</td>
		</tr>
	</s:iterator>
</table>

<h1>Sessions</h1>

<table border="1">
	<tr>
		<th>Scenario</th>
		<th>Player</th>
		<th>Baseline Difficulty</th>
		<th>Profile</th>
		<th>Rounds</th>
	</tr>
	<s:iterator value="sessions">
		<tr>
			<td><a href="#scenario-<s:property value="scenarioSession.id" />"><s:property value="scenarioSession.id" /></a></td>
			<td><a href="#player-<s:property value="player.id" />"><s:property value="player.id" /> (<s:property value="player.name" />)</a></td>
			<td><s:property value="baseline" /></td>
			<td>
			<table border="1">
				<s:iterator value="answers">
					<tr>
						<td><s:property value="question.text" /></td>
						<td><s:property value="answer" /></td>
					</tr>
				</s:iterator>
			</table>
			</td>
			<td>
			<table border="1">
				<tr>
					<th>ID</th>
					<th>Sc. Round</th>
					<th>Actual Playtime</th>
					<th>Collisions</th>
					<th>Answers</th>
					<th>Start Time</th>
					<th>Score</th>
					<th>Rank</th>
				</tr>
				<s:iterator value="rounds">
					<tr>
						<td><s:property value="id" /></td>
						<td><a href="#scenarioround-<s:property value="scenarioRound.id" />"><s:property value="scenarioRound.id" /></a></td>
						<td><s:property value="actualPlaytime" /></td>
						<td><s:property value="collisions" /></td>
						<td>
						<table border="1">
							<s:iterator value="answers">
								<tr>
									<td><s:property value="question.text" /></td>
									<td><s:property value="answer" /></td>
								</tr>
							</s:iterator>
						</table>
						</td>
						<td><s:property value="startTime" /></td>
						<td><s:property value="score.score" /></td>
						<td><s:property value="socialRank" /></td>
						<td><s:property value="globalRank" /></td>
					</tr>
				</s:iterator>
			</table>
			</td>
		</tr>
	</s:iterator>
</table>

</body>
</html>
