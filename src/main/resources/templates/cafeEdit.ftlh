<#import "parts/common.ftlh" as c>

<@c.page>
    <h5>Редактирование кофейни</h5>
    <div class="form-group w-50">
        <form method="post">
            <input type="text" class="form-control mb-1 mt-2" name="address" value="${cafe.address}" />
            <input type="text" class="form-control mb-1" name="location" value="${cafe.location}" />

            <input type="hidden" name="_csrf" value="${_csrf.token}" />
            <button type="submit" class="btn btn-primary my-2">Сохранить</button>
        </form>

        <form action="/cafe/${cafe.id}/delete" method="post">
            <input type="hidden" name="_csrf" value="${_csrf.token}" />
            <button type="submit" class="btn btn-danger">Удалить</button>
        </form>
    </div>
</@c.page>