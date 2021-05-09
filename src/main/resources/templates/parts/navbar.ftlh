<#include "security.ftlh">
<#import "login.ftlh" as l>

<nav class="navbar navbar-expand-lg navbar-light bg-light">
    <a class="navbar-brand" href="/">DrinksMachine</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <#if user??>
            <ul class="navbar-nav mr-auto">
                <li class="nav-item">
                    <a class="nav-link" href="/admin">Администратор</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/staff">Персонал</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/cafe">Кофейни</a>
                </li>

                <li class="nav-item">
                    <a class="nav-link" href="/barista">Бариста</a>
                </li>
            </ul>
            <div class="navbar-text mr-3">${name}</div>
            <@l.logout />
        </#if>
    </div>
</nav>