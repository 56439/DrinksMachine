<#import "parts/common.ftlh" as c>

<@c.page>
    <h5>Admin page</h5>
    <a class="btn btn-primary" data-toggle="collapse" href="#addDrinkCollapse" role="button" aria-expanded="false" aria-controls="addDrinkCollapse">
        Добавить
    </a>
    <div class="collapse" id="addDrinkCollapse">
        <div class="form-group w-50">
            <form method="post" enctype="multipart/form-data">
                <input type="text" class="form-control mb-1 mt-2" name="title" placeholder="Название" required/>
                <input type="text" class="form-control mb-1" name="description" placeholder="Описание" required/>
                <input type="file" class="form-control-file" name="file" required>

                <div class="mt-2">
                    <#list capacities as capacity>
                        <div class="form-row align-items-center">
                            <div class="col-sm-3">
                                <label>
                                    <input type="checkbox" name="${capacity.id}" checked>
                                    ${capacity.symbol} - ${capacity.value} мл.
                                </label>
                            </div>
                            <div class="col-auto">
                                <input class="form-control mb-1" type="number" name="cost_${capacity.id}" placeholder="Стоимость" />
                            </div>
                        </div>
                    </#list>
                </div>

                <input type="hidden" name="_csrf" value="${_csrf.token}" />
                <button type="submit" class="btn btn-primary">Добавить</button>
            </form>
        </div>
    </div>

   <div class="card-columns">
       <#list drinks as drink>
           <div class="card my-3">
               <img src="${drink.img}" class="card-img-top" height="354.66">
               <div class="m-2">
                   <span><b>${drink.title}</b></span>
               </div>
               <div class="card-footer text-muted">${drink.description}
               </div>
               <div>
                   <a class="btn btn-primary col" href="/admin/drink/${drink.id}">Изменить</a>
               </div>
           </div>
       <#else>
           No Drinks :(
       </#list>
   </div>
</@c.page>