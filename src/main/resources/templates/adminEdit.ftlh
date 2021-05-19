<#import "parts/common.ftlh" as c>

<@c.page>
    <h5>Редактирование сотрудника</h5>
    <div class="form-group w-50">
        <form method="post" class="row no-gutters">
            <div class="col">
                <input type="text" class="form-control mb-2 mt-3" name="firstName" placeholder="Имя" value="${admin.firstName}" required>
            </div>
            <div class="col">
                <input type="text" class="form-control mb-2 mt-3" name="secondName" placeholder="Фамилия" value="${admin.secondName}" required>
            </div>
            <div class="col">
                <input type="text" class="form-control mb-2 mt-3" name="thirdName" placeholder="Отчество" value="${admin.thirdName}" required>
            </div>
            <div class="col-12">
                <input type="text" class="form-control mb-2" name="username" placeholder="Логин" value="${admin.username}" required>
            </div>
            <div class="col-12">
                <input type="text" class="form-control mb-2" name="password" placeholder="Пароль" value="${admin.password}" required>
            </div>

            <input type="hidden" name="_csrf" value="${_csrf.token}" />
            <button type="submit" class="btn btn-primary my-2">Сохранить</button>
        </form>

        <form action="/staff/${admin.id}/delete" method="post">
            <input type="hidden" name="_csrf" value="${_csrf.token}" />
            <button type="submit" class="btn btn-danger">Удалить</button>
        </form>
    </div>
</@c.page>