<#include "layout.ftl">

<@layout submitAction="/crawl/getProjectList.do" filter="cert">
    第<input id="pageInput" type="text" value="${content.page}" style="width:30px"/>页 / ${content.total} 页
    <table>
        <#if content.content??>
            <#list content.content as p>
                <#if p_index % 4 == 0>
                    <tr>
                </#if>
                <td>${p}</td>
                <#if p_index % 4 == 3 || !p_has_next>
                    </tr>
                </#if>
            </#list>
        <#else >
            <h2>无法拉取到数据</h2>
            <h2>请查看<a href="http://pub.wxhouse.com/HTBA/SaleCert_List.pub">http://pub.wxhouse.com/HTBA/SaleCert_List.pub</a>是否能访问</h2>
        </#if>
    </table>

    <script type="text/javascript" >
        $("#li_overview").addClass("active");
        $("#key").val(${condition}.key);
        $("#pageInput").on("input", function() {
            $("#page").val(this.value)
        });
        $("#page").val("${content.page}");
    </script>
</@layout>
