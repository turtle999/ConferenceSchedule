package com.example.Conference.service;

import com.example.Conference.domain.Session;
import com.example.Conference.domain.Talk;
import com.example.Conference.domain.Track;
import com.example.Conference.repository.TrackRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class TrackService {
    private final TalkService talkService;
    private final TrackRepository trackRepository;
    private final ModelMapper modelMapper;

    public TrackService(TalkService talkService, TrackRepository trackRepository, ModelMapper modelMapper) {
        this.talkService = talkService;
        this.trackRepository = trackRepository;
        this.modelMapper = modelMapper;
    }

    public List<Track> getTrackList() {
        return trackRepository.findAll();
    }

    public void deleteAllTracks() {
        trackRepository.deleteAll();
    }
    public List<Track> scheduleTalks(List<Talk> talks) {
        if(talks == null || talks.isEmpty()){
            return new ArrayList<>();
        }
        List<Track> tracks =  trackRepository.findAll();
        if(!tracks.isEmpty() && talks.stream().allMatch(Talk::isScheduled)){
            return tracks;
        }else{
            trackRepository.deleteAll();
            tracks.removeAll(tracks);
            talkService.updateAllTalksToUnscheduled();
        }
        Track currentTrack = null;
        if(tracks == null || tracks.isEmpty()) {
            tracks = new ArrayList<>();
             currentTrack= new Track();
            tracks.add(currentTrack);
        }

        List<Talk> unscheduledTalks = talks.stream()
                .filter(talk -> !talk.isScheduled())
                .toList();

        while (!unscheduledTalks.isEmpty()) {
            if(currentTrack.getMorning().getRemainingDuration()>0 && !currentTrack.getMorning().isClosedWithPossibleMinDuration()){
                List<Talk> subset = findSubsetSumForSession(unscheduledTalks, currentTrack.getMorning().getRemainingDuration()) ;
                if (subset == null) {
                    subset = findClosestSubsetSumForSession(unscheduledTalks,currentTrack.getMorning().getRemainingDuration());
                    currentTrack.getMorning().setClosedWithPossibleMinDuration(true);
                }
                for (Talk talk : subset) {
                    currentTrack.getMorning().addTalk(talk);
                    talk.setScheduled(true);
                }
            } else if (currentTrack.getAfternoon().getRemainingDuration()>0 && !currentTrack.getAfternoon().isClosedWithPossibleMinDuration()) {
                List<Talk> subset = findSubsetSumForSession(unscheduledTalks, currentTrack.getAfternoon().getRemainingDuration()) ;
                if (subset == null) {
                    subset = findClosestSubsetSumForSession(unscheduledTalks,currentTrack.getAfternoon().getRemainingDuration());
                    currentTrack.getAfternoon().setClosedWithPossibleMinDuration(true);
                }
                for (Talk talk : subset) {
                    currentTrack.getAfternoon().addTalk(talk);
                    talk.setScheduled(true);
                }
            }
            unscheduledTalks = talks.stream()
                    .filter(talk -> !talk.isScheduled())
                    .toList();
            if(!unscheduledTalks.isEmpty() && ((currentTrack.getMorning().getRemainingDuration() ==0 || currentTrack.getMorning().isClosedWithPossibleMinDuration())
                    && (currentTrack.getAfternoon().getRemainingDuration()==0 || currentTrack.getAfternoon().isClosedWithPossibleMinDuration()))){
                currentTrack = new Track();
                tracks.add(currentTrack);
            }
        }

        trackRepository.saveAll(tracks);
        return tracks;
    }

    public  List<Talk> findSubsetSumForSession(List<Talk> talks, int targetSum) {
        Map<String, List<Talk>> memo = new HashMap<>();
        return findSubsetSumHelper(talks, targetSum, talks.size() - 1, memo);
    }

    private  List<Talk> findSubsetSumHelper(List<Talk> talks, int targetSum, int index, Map<String, List<Talk>> memo) {
        if (targetSum == 0) {
            return new ArrayList<>();
        }
        if (targetSum < 0 || index < 0) {
            return null;
        }

        String key = index + "|" + targetSum;
        if (memo.containsKey(key)) {
            return memo.get(key);
        }

        // Exclude the current element and proceed
        List<Talk> exclude = findSubsetSumHelper(talks, targetSum, index - 1, memo);
        if (exclude != null) {
            memo.put(key, exclude);
            return exclude;
        }

        // Include the current element and proceed
        List<Talk> include = findSubsetSumHelper(talks, targetSum - talks.get(index).getDuration(), index - 1, memo);
        if (include != null) {
            include = new ArrayList<>(include);  // Clone the list to avoid modifying the original
            include.add(talks.get(index));
            memo.put(key, include);
            return include;
        }

        memo.put(key, null);
        return null;
    }

    public  List<Talk> findClosestSubsetSumForSession(List<Talk> talks, int target) {
        int n = talks.size();
        // Initialize the DP table
        boolean[] dp = new boolean[target + 1];
        dp[0] = true; // Sum of 0 is always possible with empty subset

        // Fill the DP table
        for (Talk talk : talks) {
            for (int j = target; j >= talk.getDuration(); j--) {
                dp[j] = dp[j] || dp[j - talk.getDuration()];
            }
        }

        // Find the closest sum to the target that does not exceed it
        int closestSum = 0;
        for (int j = target; j >= 0; j--) {
            if (dp[j]) {
                closestSum = j;
                break;
            }
        }

        // Trace back to find the subset
        List<Talk> subset = new ArrayList<>();
        int currentSum = closestSum;
        for (int i = n - 1; i >= 0; i--) {
            Talk talk = talks.get(i);
            if (currentSum >= talk.getDuration() && dp[currentSum - talk.getDuration()]) {
                subset.add(talk);
                currentSum -= talk.getDuration();
            }
        }
        return subset;
    }

    public List<String> GetFormattedSchedule() {
        List<Talk> talks = talkService.getTalkList();
        List<Track> scheduledTracks = scheduleTalks(talks);
        return formatTracks(scheduledTracks);
    }

    private List<String> formatTracks(List<Track> tracks) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mma",  Locale.ENGLISH);
        List<String> formattedSchedule = new ArrayList<>();
        for (int i = 0; i < tracks.size(); i++) {
            Track track = tracks.get(i);
            formattedSchedule.add("Track " + (i + 1) + " :" );
            formattedSchedule.addAll(formatSession(track.getMorning(), LocalTime.of(9, 0), timeFormatter));
            formattedSchedule.add("12:00PM Lunch");
            formattedSchedule.addAll(formatSession(track.getAfternoon(), LocalTime.of(13, 0), timeFormatter));
            formattedSchedule.add("05:00PM Networking Event");
            if(i != tracks.size()-1){
                formattedSchedule.add("--");
            }
        }
        return formattedSchedule;
    }

    private Collection<String> formatSession(Session session, LocalTime startTime, DateTimeFormatter timeFormatter) {
        List<String> formattedSession = new ArrayList<>();
        for (Talk talk : session.getTalks()) {
            String duration = talk.getDuration() == 5 ? "lightning" : talk.getDuration() + "min";
            formattedSession.add(startTime.format(timeFormatter) + " " + talk.getTitle() + " " + duration);
            startTime = startTime.plusMinutes(talk.getDuration());
        }
        return formattedSession;
    }

}
