<#import "parts/common.ftlh" as c>

<@c.page>
    <h5>Редактирование сотрудника</h5>
    <div class="form-group w-50">
        <form method="post">
            <input class="form-control mb-1 mt-2" type="text" name="username" value="${staff.username}">
            <input class="form-control mb-1" type="text" name="password" value="${staff.password}">

            <#list roles as role>
                <div class="form-row align-items-center">
                    <div class="col-sm-2">
                        <label>
                            <input type="checkbox" name="${role}" ${staff.roles?seq_contains(role)?string("checked", "")}>
                            ${role}
                        </label>
                    </div>
                </div>
            </#list>

            <input type="hidden" name="staffId" value="${staff.id}">
            <input type="hidden" name="_csrf" value="${_csrf.token}" />
            <button type="submit" class="btn btn-primary my-2">Сохранить</button>
        </form>

        <form action="/staff/${staff.id}/delete" method="post">
            <input type="hidden" name="_csrf" value="${_csrf.token}" />
            <button type="submit" class="btn btn-danger">Удалить</button>
        </form>
    </div>
</@c.page>