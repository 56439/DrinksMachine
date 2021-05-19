<#import "parts/common.ftlh" as c>

<@c.page>
    <h5>Редактирование бариста</h5>
    <div class="form-group w-50">
        <form method="post" class="row no-gutters">
            <div class="col">
                <input type="text" class="form-control mb-2 mt-3" name="firstName" placeholder="Имя"
                       value="${barista.firstName}" required>
            </div>
            <div class="col">
                <input type="text" class="form-control mb-2 mt-3" name="secondName" placeholder="Фамилия"
                       value="${barista.secondName}" required>
            </div>
            <div class="col">
                <input type="text" class="form-control mb-2 mt-3" name="thirdName" placeholder="Отчество"
                       value="${barista.thirdName}" required>
            </div>
            <div class="col-12">
                <input type="text" class="form-control mb-2" name="username" placeholder="Логин"
                       value="${barista.username}" required>
            </div>
            <div class="col-12">
                <input type="text" class="form-control mb-2" name="password" placeholder="Пароль"
                       value="${barista.password}" required>
            </div>
            <div class="col-12">
                <select class="form-control mb-2" name="cafe" onselect="" required>
                    <#list cafe as c>
                        <option ${barista.cafe.address?contains(c.address)?string("selected", "")}>
                            ${c.address}
                        </option>
                    </#list>
                </select>
            </div>

            <input type="hidden" name="_csrf" value="${_csrf.token}" />
            <button type="submit" class="btn btn-primary my-2">Сохранить</button>
        </form>

        <form action="/staff/barista/${barista.id}/delete" method="post">
            <input type="hidden" name="_csrf" value="${_csrf.token}" />
            <button type="submit" class="btn btn-danger">Удалить</button>
        </form>
    </div>
</@c.page>