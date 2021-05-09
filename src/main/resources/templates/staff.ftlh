<#import "parts/common.ftlh" as c>

<@c.page>
    <h5>Сотрудники</h5>
    <a class="btn btn-primary" data-toggle="collapse" href="#addStaffCollapse" role="button" aria-expanded="false" aria-controls="addStaffCollapse">
        Добавить
    </a>
    <div class="collapse" id="addStaffCollapse">
        <div class="form-group w-50">
            <form action="/staff" method="post">
                <input type="text" class="form-control mb-1 mt-2" name="username" placeholder="Логин">
                <input type="text" class="form-control mb-1" name="password" placeholder="Пароль">

                <#list roles as role>
                    <div class="form-row align-items-center">
                        <div class="col-sm-2">
                            <label>
                                <input type="checkbox" name="${role}">
                                ${role}
                            </label>
                        </div>
                    </div>

                </#list>

                <input type="hidden" name="_csrf" value="${_csrf.token}" />
                <button type="submit" class="btn btn-primary">Добавить</button>
            </form>
        </div>
    </div>

    <div class="card w-50 my-3">
        <ul class="list-group list-group-flush">
            <#list staff as s>
                    <li class="list-group-item">
                        <div class="form-row">
                            <div class="col-sm-2">
                                ${s.username}
                            </div>
                            <div class="col-auto">
                                <a class="btn btn-primary" href="/staff/${s.id}">Изменить</a>
                            </div>
                        </div>
                    </li>
            </#list>
        </ul>
    </div>
</@c.page>