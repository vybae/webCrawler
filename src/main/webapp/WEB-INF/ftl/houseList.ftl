<#include "layout.ftl">

<@layout submitAction="/crawl/getHouseList.do" filter="house">
    <table>
       ${certno} <br>
        物业类型  |  已售数  |  总数  |  销售面积  | 总面积 <br>
        <#list statis.entrySet() as entry>
            ${entry.key} | ${entry.value[0]} | ${entry.value[1]} | ${entry.value[2]} | ${entry.value[3]} <br>
       </#list>

    </table>

    <script type="text/javascript" >
        $("#li_analysis").addClass("active");
        var cond = ${queryCondition};
    </script>
</@layout>