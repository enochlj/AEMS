<#--
/*
 * By Huang Linjie
 */
-->
<#if (actionErrors?? && actionErrors?size > 0)>
	<#list actionErrors as error>
		<#if error?if_exists != "">
            <#if parameters.escape>${error!?html}<#else>${error!}</#if><#rt/><#rt/>
        </#if>
	</#list>
</#if>