<template>
  <div ref="gantt"></div>
</template>

<script>
import 'dhtmlx-gantt'
export default {
  name: 'gantt',
  props: {
    tasks: {
      type: Object,
      default () {
        return {data: [], links: []}
      }
    }
  },

  mounted(){
    this.init();
  },
  created(){
    gantt.config.drag_progress = false;
    gantt.config.drag_move = false;
    gantt.config.drag_links = false;
    this.setScaleConfig("day");
  },
  watch:{
    tasks:{
      handler:function(n,o){
        this.init();
      },
      deep:true
    }
  },
  methods:{
    init(){
      gantt.i18n.setLocale("cn");
      gantt.clearAll()
      gantt.init(this.$refs.gantt)
      gantt.parse(this.tasks)
    },
    setScaleConfig(level) {
      switch (level) {
        case "day":
          gantt.config.scales = [{ unit: "day", step: 1, format: "%M%d日" }];
          gantt.config.scale_height = 27;
          break;
        case "week":
          var weekScaleTemplate = function (date) {
            var dateToStr = gantt.date.date_to_str("%d %M");
            var endDate = gantt.date.add(
              gantt.date.add(date, 1, "week"),
              -1,
              "day"
            );
            return dateToStr(date) + " - " + dateToStr(endDate);
          };
          gantt.config.scales = [
            { unit: "week", step: 1, format: weekScaleTemplate },
          ];
          gantt.config.scale_height = 27;
          break;
        case "month":
          gantt.config.scales = [{ unit: "month", step: 1, format: "%F, %Y" }];
          gantt.config.scale_height = 27;
          break;
        case "year":
          gantt.config.scales = [{ unit: "year", step: 1, format: "%Y" }];
          gantt.config.scale_height = 27;
          break;
      }
    },
  }
}
</script>

<style>
    @import "~dhtmlx-gantt/codebase/dhtmlxgantt.css";
</style>
