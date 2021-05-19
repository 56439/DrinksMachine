<#import "parts/common.ftlh" as c>

<@c.page>
    <h5>Редактирование кофейни</h5>

    <div class="form-group w-50">
        <form method="post" class="row no-gutters">
            <input type="text" class="form-control mb-1 mt-2" name="address" value="${cafe.address}" required/>
            <input type="text" class="form-control mb-2" name="location" value="${cafe.location}" required/>

            <div class="col">
                <input class="form-control" name="openTime" type="time" value="0${cafe.openTime}"
                       min="08:00" max="09:00" required>
                <small>Открытие</small>
            </div>
            <div class="col ml-1">
                <input class="form-control" name="closeTime" type="time" value="${cafe.closeTime}"
                       min="18:00" max="20:00" required>
                <small>Закрытие</small>
            </div>
            <div class="col ml-1">
                <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                <button type="submit" class="btn btn-primary">Сохранить</button>
            </div>
        </form>
        <form action="/cafe/${cafe.id}/delete" method="post">
            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
            <button type="submit" class="btn btn-danger">Удалить</button>
        </form>
    </div>
</@c.page>