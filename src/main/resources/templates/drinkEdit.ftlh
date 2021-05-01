<#import "parts/common.ftlh" as c>

<@c.page>
    <h5>Редактирование напитка</h5>
    <div class="form-group w-50">
        <form method="post">
            <label>ID: <b>${drink.id}</b></label>
            <input type="text" class="form-control mb-1 mt-2" name="title" value="${drink.title}">
            <input type="text" class="form-control mb-1" name="description" value="${drink.description}">
            <input type="text" class="form-control mb-1" name="img" value="${drink.img}">

            <#list capacities as capacity>
                <div class="form-row align-items-center">
                    <div class="col-sm-3">
                        <label>
                            <input type="checkbox" name="${capacity.id}"
                                   <#list menu as m><#if m.capacity == capacity>checked</#if></#list>>
                            ${capacity.symbol} - ${capacity.value} мл.
                        </label>
                    </div>
                    <div class="col-auto">
                        <input type="number" class="form-control" name="cost_${capacity.id}" placeholder="Стоимость"
                               value="<#list menu as m><#if m.capacity == capacity>${m.cost}</#if></#list>">
                    </div>
                </div>
            </#list>
            <input type="hidden" name="_csrf" value="${_csrf.token}" />
            <button type="submit" class="btn btn-primary my-2">Сохранить</button>
        </form>

        <form action="/admin/drink/${drink.id}/delete" method="post">
            <input type="hidden" name="_csrf" value="${_csrf.token}" />
            <button type="submit" class="btn btn-danger">Удалить</button>
        </form>
    </div>
</@c.page>