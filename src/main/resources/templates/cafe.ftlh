<#import "parts/common.ftlh" as c>

<@c.page>
    <h5>Кофейни</h5>
    <a class="btn btn-primary" data-toggle="collapse" href="#addAddressCollapse"
       role="button" aria-expanded="false" aria-controls="addAddressCollapse">
        Добавить
    </a>
    <div class="collapse" id="addAddressCollapse">
        <div class="form-group w-50">
            <form method="post" class="row no-gutters">
                <input type="text" class="form-control mb-1 mt-2" name="address" placeholder="Адрес" required/>
                <input type="text" class="form-control mb-2" name="location" placeholder="Координаты" required/>

                <div class="col">
                    <input class="form-control" name="openTime" type="time" value="08:00"
                           min="08:00" max="10:00" required>
                    <small>Открытие</small>
                </div>
                <div class="col ml-1">
                    <input class="form-control" name="closeTime" type="time" value="19:00"
                           min="18:00" max="21:00" required>
                    <small>Закрытие</small>
                </div>
                <div class="col ml-1">
                    <input type="hidden" name="_csrf" value="${_csrf.token}" />
                    <button type="submit" class="btn btn-primary">Добавить</button>
                </div>
            </form>
        </div>
    </div>

    <div class="card w-50 my-3">
        <ul class="list-group list-group-flush">
            <#list cafes as cafe>
                <li class="list-group-item">
                    <div class="form-row">
                        <div class="col-sm-5">
                            ${cafe.address}
                        </div>
                        <div class="col-sm-4">
                            ${cafe.openTime} - ${cafe.closeTime}
                        </div>
                        <div class="col-auto">
                            <a class="btn btn-primary" href="/cafe/${cafe.id}">Изменить</a>
                        </div>
                    </div>
                </li>
            <#else>
                No Addresses :(
            </#list>
        </ul>
    </div>
</@c.page>