package sample;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import static sample.Util.isOverlaps;

public class ScoreModel {
    private List<ScheduleModel> schedules;
    private double score;
    private String display;

    public ScoreModel(List<ScheduleModel> schedules) {
        this.schedules = schedules;
        this.score = calculateScore(schedules);
        this.display = createDisplay(schedules);
    }

    public List<ScheduleModel> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<ScheduleModel> schedules) {
        this.schedules = schedules;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
    public String createDisplay(List<ScheduleModel> schedules){
        if (schedules == null || schedules.size() == 0) return "";
        String display = "";

        Collections.sort(schedules, new Comparator<ScheduleModel>() {
                    @Override
                    public int compare(ScheduleModel o1, ScheduleModel o2) {
                        LocalDateTime x1 = o1.getDate().atTime(o1.getStartAtHour(),o1.getStartAtMinute());
                        LocalDateTime x2 = o2.getDate().atTime(o2.getStartAtHour(),o2.getStartAtMinute());
                        int result = x1.toLocalDate().compareTo( x2.toLocalDate() );
                        return result;
                    }
                }
        );
        for (var item: schedules){
            display += "• " + item.getSubjectId().getName() + "   " +
                item.getDate() + " " + item.getStartAtHour() + ":" + item.getStartAtMinute() + " - " +
                    item.getEndAtHour() + ":" + item.getEndAtMinute() + "\n\n";
        }
        return display;
    }

    public static Comparator<ScoreModel> COMPARE_BY_SCORE = new Comparator<ScoreModel>() {
        @Override
        public int compare(ScoreModel o1, ScoreModel o2) {
            return Double.compare(o1.getScore(), o2.getScore());
        }
    };

    public double calculateScore(List<ScheduleModel> schedules){
        if (schedules == null || schedules.size() == 0) return 0.0;
        DecimalFormat df = new DecimalFormat("###.###");

        int originSize = schedules.size();
        for (var item: schedules){
            System.out.println("&&&&&&^");
            System.out.println(item.getDate());
            System.out.println(item.getStartAtHour());
            System.out.println(item.getStartAtMinute());
            System.out.println(item.getEndAtHour());
            System.out.println(item.getEndAtMinute());
            System.out.println(item.getSubjectId().getName());
            System.out.println("&&&&&&");
            System.out.println(item.getId());
            System.out.println("\n");
        }

        List<ScheduleModel> list = new ArrayList<>();
        for (var item: schedules){
            System.out.println(item);
            boolean isDuplicateTime = false;
            for(int i = 0 ; i< schedules.size(); i++){
                // not check same model
                if (item.getId() == schedules.get(i).getId()){
                    continue;
                }
                boolean checkIsOverLaps = isOverlaps(
                        item.getStartAtHour(), item.getStartAtMinute(), item.getEndAtHour(), item.getEndAtMinute(),
                        schedules.get(i).getStartAtHour(), schedules.get(i).getStartAtMinute(), schedules.get(i).getEndAtHour(), schedules.get(i).getEndAtMinute());
                System.out.println("\n");
                System.out.println(item.getDate());
                System.out.println(item.getStartAtHour());
                System.out.println(item.getStartAtMinute());
                System.out.println(item.getEndAtHour());
                System.out.println(item.getEndAtMinute());

                System.out.println("--------");
                System.out.println(checkIsOverLaps);
                System.out.println((item.getDate().equals(schedules.get(i).getDate())));
                System.out.println(item.getDate());
                System.out.println(schedules.get(i).getDate());
                System.out.println("--------");
                System.out.println(schedules.get(i).getDate());
                System.out.println(schedules.get(i).getStartAtHour());
                System.out.println(schedules.get(i).getStartAtMinute());
                System.out.println(schedules.get(i).getEndAtHour());
                System.out.println(schedules.get(i).getEndAtMinute());


                if ((item.getDate().equals(schedules.get(i).getDate())) && checkIsOverLaps){
                    System.out.println("isDuplicateTime = true");

                    isDuplicateTime = true;
                    break;
                }else                     System.out.println("isDuplicateTime = false");


                System.out.println("\n");
            }
            if (isDuplicateTime == false || ( !isOverlap(list, item) ) ){
                System.out.println("isDuplicateTime = false || ( !isOverlap(list, item)");
                list.add(item);
            }else {
                System.out.println("NOT isDuplicateTime = false || ( !isOverlap(list, item)");
            }
        }

        String formatScrore = df.format(
                (( Double.valueOf(list.size())  / Double.valueOf(originSize)  )*100)
        );

        return Double.parseDouble(formatScrore);


    }

    public boolean isOverlap(List<ScheduleModel> list, ScheduleModel scheduleModel){
        if (list == null || list.size() == 0 ) return false;
        for(var item: list){
            if ( (item.getDate().equals(scheduleModel.getDate())) && isOverlaps(
                    scheduleModel.getStartAtHour(), scheduleModel.getStartAtMinute(), scheduleModel.getEndAtHour(), scheduleModel.getEndAtMinute(),
                    item.getStartAtHour(), item.getStartAtMinute(), item.getEndAtHour(), item.getEndAtMinute())
            ) {
                return true;
            }
        }
        return false;

    }


    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }
}
