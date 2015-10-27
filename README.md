# Slacker Poller Plugin

This is a plugin for the [Slacker](https://github.com/mdome7/slacker) project.

## Configuration Example:

```
  - name: List Polls
    alias: poll list
    plugin: slacker-plugin-poller-1.0-SNAPSHOT
    className: com.labs2160.slacker.plugin.poller.ListPollsAction

  - name: Create Poll
    alias: poll create
    plugin: slacker-plugin-poller-1.0-SNAPSHOT
    className: com.labs2160.slacker.plugin.poller.CreatePollAction

  - name: Add Poll Option
    alias: poll add-option
    plugin: slacker-plugin-poller-1.0-SNAPSHOT
    className: com.labs2160.slacker.plugin.poller.AddOptionAction

  - name: Show Poll
    alias: poll show
    plugin: slacker-plugin-poller-1.0-SNAPSHOT
    className: com.labs2160.slacker.plugin.poller.ShowPollAction

  - name: Poll Vote
    alias: poll vote
    plugin: slacker-plugin-poller-1.0-SNAPSHOT
    className: com.labs2160.slacker.plugin.poller.PollVoteAction

  - name: Close Poll
    alias: poll close
    plugin: slacker-plugin-poller-1.0-SNAPSHOT
    className: com.labs2160.slacker.plugin.poller.ClosePollAction

  - name: Delete Poll
    alias: poll delete
    plugin: slacker-plugin-poller-1.0-SNAPSHOT
    className: com.labs2160.slacker.plugin.poller.DeletePollAction
```

## Request Examples

```
poll create firstPoll this is my 1st poll
poll create secondPoll What's your favorite fruit?
poll add-option secondPoll a apple
poll add-option secondPoll b banana
poll list
   firstPoll - this is my 1st poll
   secondPoll - What's your favorite fruit?
poll close firstPoll
poll list
   firstPoll (CLOSED) - this is my 1st poll
   secondPoll - What's your favorite fruit?
poll show secondPoll
   What's your favorite fruit?
     a) apple
     b) banana
poll vote secondPoll a

poll show-results secondPoll
   What's your favorite fruit?
     7 votes - a) apple
     0 votes - b) banana

poll close secondPoll
poll show secondPoll
   What's your favorite fruit?
   *** CLOSED on blah ****
   !!! Winner: a !!!
     7 votes - a) apple
     0 votes - b) banana

poll list
   firstPoll (CLOSED) - this is my 1st poll
   secondPoll (CLOSED) - What's your favorite fruit?
poll delete secondPoll
poll list
   firstPoll (CLOSED) - this is my 1st poll
```
