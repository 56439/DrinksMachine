<#import "parts/common.ftlh" as c>

<@c.page>
    <h5>Кофейни</h5>
    <a class="btn btn-primary" data-toggle="collapse" href="#addAddressCollapse" role="button" aria-expanded="false" aria-controls="addAddressCollapse">Добавить
    </a>
    <div class="collapse" id="addAddressCollapse">
        <div class="form-group w-50">
            <form method="post">
                <input type="text" class="form-control mb-1 mt-2" name="address" placeholder="Адрес" />
                <input type="text" class="form-control mb-1" name="location" placeholder="Координаты" />

                <input type="hidden" name="_csrf" value="${_csrf.token}" />
                <button type="submit" class="btn btn-primary">Добавить</button>
            </form>
        </div>
    </div>

    <div class="card w-50 my-3">
        <ul class="list-group list-group-flush">
            <#list cafes as cafe>
                <li class="list-group-item">
                    <div class="form-row">
                        <div class="col-sm-6">
                            ${cafe.address}
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