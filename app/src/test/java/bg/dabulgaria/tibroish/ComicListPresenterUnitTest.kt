//package bg.dabulgaria.tibroish
//
//import bg.dabulgaria.tibroish.domain.interactors.ComicInteractor
//import bg.dabulgaria.tibroish.domain.interactors.IComicInteractor
//import bg.dabulgaria.tibroish.domain.models.Comic
//import bg.dabulgaria.tibroish.domain.models.ComicList
//import bg.dabulgaria.tibroish.domain.providers.ILogger
//import bg.dabulgaria.tibroish.domain.repositories.remote.IMarvelsRemoteRepository
//import bg.dabulgaria.tibroish.domain.repositories.local.IComicsLocalRepository
//import bg.dabulgaria.tibroish.presentation.comic.list.presenter.ComicListPresenter
//import bg.dabulgaria.tibroish.presentation.comic.list.view.IComicListView
//import bg.dabulgaria.tibroish.presentation.comic.list.model.ComicListViewModel
//import bg.dabulgaria.tibroish.presentation.providers.INetworkInfoProvider
//import bg.dabulgaria.tibroish.presentation.providers.IResourceProvider
//import bg.dabulgaria.tibroish.utils.ISchedulersProvider
//
//import org.junit.*
//import org.mockito.invocation.InvocationOnMock
//import org.mockito.stubbing.Answer
//import com.nhaarman.mockitokotlin2.any
//import io.reactivex.rxjava3.core.Single
//import io.reactivex.rxjava3.schedulers.TestScheduler
//
//import org.mockito.*
//import org.mockito.Mockito.*
//
//class ComicListPresenterUnitTest {
//
//    @Mock
//    lateinit var comicView: IComicListView
//
//    @Mock
//    lateinit var resourceProvider : IResourceProvider
//
//    @Mock
//    val logger : ILogger? = null
//
//    @Mock
//    lateinit var networkInfoProvider : INetworkInfoProvider
//
//    @Mock
//    lateinit var comicsLocalRepository: IComicsLocalRepository
//
//    @Mock
//    lateinit var mMarvelsRemoteRepository: IMarvelsRemoteRepository
//
//    @Mock
//    lateinit var comicInteractorMock: IComicInteractor
//
//    @Mock
//    lateinit var schedulersProvider: ISchedulersProvider
//
//    @InjectMocks
//    lateinit var comicPresenter: ComicListPresenter
//
//    @InjectMocks
//    lateinit var comicInteractor: ComicInteractor
//
//    private val testScheduler = TestScheduler()
//
//    private val dbItems = ComicList()
//    private val serverItems = mutableListOf<Comic>()
//
//    lateinit var comicRemote:Comic
//    lateinit var comicData:Comic
//
//    @Before
//    fun setup() {
//
//        MockitoAnnotations.initMocks(this)
//
//        doReturn(testScheduler).`when`<ISchedulersProvider>(schedulersProvider).ioScheduler()
//        doReturn(testScheduler).`when`<ISchedulersProvider>(schedulersProvider).uiScheduler()
//        doReturn(testScheduler).`when`<ISchedulersProvider>(schedulersProvider).computationScheduler()
//
//        comicRemote = Comic()
//
//        comicRemote.id = 2
//        comicRemote.title = "Title2"
//        comicRemote.description = "description2"
//        comicRemote.thumbnailUrl = "http://i.annihil.us/u/prod/marvel/i/mg/b/40/image_not_available.jpg"
//
//        comicData = Comic()
//        comicData.id = 1
//        comicData.title = "TITLE1"
//        comicData.description = "description1"
//        comicData.thumbnailUrl ="http://i.annihil.us/u/prod/marvel/i/mg/b/40/image_not_available.jpg"
//
//        val dbData = mutableListOf<Comic>()
//        dbData.add(comicData)
//        dbItems.comics = dbData
//        serverItems.add(comicRemote)
//    }
//
//    @Test
//    fun Should_Load_Comics_List_Into_View() {
//
//        doReturn(Single.just(dbItems)).`when`<IComicsLocalRepository>(comicsLocalRepository).getComicList()
//        doReturn(Single.just<List<Comic>>(serverItems)).`when`<IMarvelsRemoteRepository>(mMarvelsRemoteRepository).getComicsList( any() )
//
//        doAnswer(object : Answer<Single<ComicList>> {
//            @Throws(Throwable::class)
//            override fun answer(invocation: InvocationOnMock): Single<ComicList> {
//                return comicInteractor.getComicList( false, 20L)
//            }
//        }).`when`<IComicInteractor>(comicInteractorMock).getComicList( any(), any() )
//
//        val viewModelCaptor = com.nhaarman.mockitokotlin2.argumentCaptor<ComicListViewModel>()
//
//        comicPresenter.loadComics(false)
//        testScheduler.triggerActions()
//
//        Mockito.verify<IComicListView>(comicView).onComicsLoaded( viewModelCaptor.capture())
//
//        Assert.assertEquals( 1, viewModelCaptor.allValues.size )
//        Assert.assertEquals( 1, viewModelCaptor.firstValue.list.size )
//
//        verify<IComicListView>(comicView, times(0)).onError(any())
//        verify<IComicListView>(comicView, times(1)).onLoadingStateChange(any())
//        verify<IComicListView>(comicView, times(1)).onComicsLoaded(any())
//
//        verify<IComicsLocalRepository>( comicsLocalRepository, times( 1)).getComicList()
//        verify<IComicsLocalRepository>( comicsLocalRepository, times( 0)).getComic(any())
//        verify<IComicsLocalRepository>( comicsLocalRepository, times( 0)).updateComic(any())
//        verify<IComicsLocalRepository>( comicsLocalRepository, times( 0)).updateComicList(any())
//
//        verify<IMarvelsRemoteRepository>( mMarvelsRemoteRepository, times( 0)).getComicDetails( any() )
//        verify<IMarvelsRemoteRepository>( mMarvelsRemoteRepository, times( 0)).getComicsList( any() )
//    }
//
//    @Test
//    fun Should_Load_Comics_List_From_Database() {
//
//        doReturn(Single.just(dbItems)).`when`<IComicsLocalRepository>(comicsLocalRepository).getComicList()
//        doReturn(Single.just<List<Comic>>(serverItems)).`when`<IMarvelsRemoteRepository>(mMarvelsRemoteRepository).getComicsList( any() )
//
//        doAnswer(object : Answer  <Single<ComicList>> {
//            @Throws(Throwable::class)
//            override fun answer(invocation: InvocationOnMock):  Single<ComicList> {
//                return comicInteractor.getComicList( false, 20 )
//            }
//        }).`when`<IComicInteractor>(comicInteractorMock).getComicList( any(), any() )
//
//        val viewModelCaptor = com.nhaarman.mockitokotlin2.argumentCaptor<ComicListViewModel>()
//
//        comicPresenter.loadComics(false)
//        testScheduler.triggerActions()
//
//        Mockito.verify<IComicListView>(comicView).onComicsLoaded( viewModelCaptor.capture())
//
//        Assert.assertEquals( 1, viewModelCaptor.allValues.size )
//        Assert.assertEquals( 1, viewModelCaptor.firstValue.list.size )
//
//        Assert.assertEquals("Item not loaded from Db", comicData.title, viewModelCaptor.firstValue.list.get(0).title )
//
//        verify<IComicListView>(comicView, times(0)).onError(any())
//        verify<IComicListView>(comicView, times(1)).onLoadingStateChange(any())
//        verify<IComicListView>(comicView, times(1)).onComicsLoaded(any())
//
//        verify<IComicsLocalRepository>( comicsLocalRepository, times( 1)).getComicList()
//        verify<IComicsLocalRepository>( comicsLocalRepository, times( 0)).getComic(any())
//        verify<IComicsLocalRepository>( comicsLocalRepository, times( 0)).updateComic(any())
//        verify<IComicsLocalRepository>( comicsLocalRepository, times( 0)).updateComicList(any())
//
//        verify<IMarvelsRemoteRepository>( mMarvelsRemoteRepository, times( 0)).getComicDetails( any() )
//        verify<IMarvelsRemoteRepository>( mMarvelsRemoteRepository, times( 0)).getComicsList( any() )
//    }
//
//    @Test
//    fun Should_Load_Comics_List_From_Server_Refresh_True() {
//
//        doReturn(Single.just(dbItems)).`when`<IComicsLocalRepository>(comicsLocalRepository).getComicList()
//        doReturn(Single.just<List<Comic>>(serverItems)).`when`<IMarvelsRemoteRepository>(mMarvelsRemoteRepository).getComicsList( any() )
//
//        doAnswer(object : Answer <Single<ComicList> > {
//            @Throws(Throwable::class)
//            override fun answer(invocation: InvocationOnMock): Single<ComicList>  {
//                return comicInteractor.getComicList( true, 20 )
//            }
//        }).`when`<IComicInteractor>(comicInteractorMock).getComicList( any(),any() )
//
//        doAnswer(object : Answer <Single<ComicList> > {
//            @Throws(Throwable::class)
//            override fun answer(invocation: InvocationOnMock): Single<ComicList>   {
//
//                val comic = invocation.getArgument<ComicList>(0)
//                return Single.just(comic)
//            }
//        }).`when`<IComicsLocalRepository>(comicsLocalRepository).updateComicList( any() )
//
//        val viewModelCaptor = com.nhaarman.mockitokotlin2.argumentCaptor<ComicListViewModel>()
//
//        comicPresenter.loadComics(true )
//        testScheduler.triggerActions()
//
//        Mockito.verify<IComicListView>(comicView).onComicsLoaded( viewModelCaptor.capture())
//
//        Assert.assertEquals( 1, viewModelCaptor.allValues.size )
//        Assert.assertEquals( 1, viewModelCaptor.firstValue.list.size )
//
//        Assert.assertEquals("Item not loaded from Server", comicRemote.title, viewModelCaptor.firstValue.list.get(0).title )
//
//        verify<IComicListView>(comicView, times(0)).onError(any())
//        verify<IComicListView>(comicView, times(1)).onLoadingStateChange(any())
//        verify<IComicListView>(comicView, times(1)).onComicsLoaded(any())
//
//        verify<IComicsLocalRepository>( comicsLocalRepository, times( 0)).getComicList()
//        verify<IComicsLocalRepository>( comicsLocalRepository, times( 0)).getComic(any())
//        verify<IComicsLocalRepository>( comicsLocalRepository, times( 0)).updateComic(any())
//        verify<IComicsLocalRepository>( comicsLocalRepository, times( 1)).updateComicList(any())
//
//        verify<IMarvelsRemoteRepository>( mMarvelsRemoteRepository, times( 0)).getComicDetails( any() )
//        verify<IMarvelsRemoteRepository>( mMarvelsRemoteRepository, times( 1)).getComicsList( any() )
//    }
//
//    @Test
//    fun Should_Load_Comics_List_From_Server_Refresh_False_No_Db_Items() {
//
//        doReturn(Single.just(ComicList.empty())).`when`<IComicsLocalRepository>(comicsLocalRepository).getComicList()
//        doReturn(Single.just<List<Comic>>(serverItems)).`when`<IMarvelsRemoteRepository>(mMarvelsRemoteRepository).getComicsList( any() )
//
//        doAnswer(object : Answer <Any> {
//            @Throws(Throwable::class)
//            override fun answer(invocation: InvocationOnMock): Any {
//                return comicInteractor.getComicList( false, 20 )
//            }
//        }).`when`<IComicInteractor>(comicInteractorMock).getComicList( any(), any())
//
//        doAnswer(object : Answer <Single<ComicList> >{
//            @Throws(Throwable::class)
//            override fun answer(invocation: InvocationOnMock): Single<ComicList>  {
//
//                val comic = invocation.getArgument<ComicList>(0)
//                return Single.just(comic)
//            }
//        }).`when`<IComicsLocalRepository>(comicsLocalRepository).updateComicList( any() )
//
//        val viewModelCaptor = com.nhaarman.mockitokotlin2.argumentCaptor<ComicListViewModel>()
//
//        comicPresenter.loadComics(false)
//        testScheduler.triggerActions()
//
//        Mockito.verify<IComicListView>(comicView).onComicsLoaded( viewModelCaptor.capture())
//
//        Assert.assertEquals( 1, viewModelCaptor.allValues.size )
//        Assert.assertEquals( 1, viewModelCaptor.firstValue.list.size )
//
//        Assert.assertEquals("Item not loaded from Server", "Title2", viewModelCaptor.firstValue.list.get(0).title )
//
//        verify<IComicListView>(comicView, times(0)).onError(any())
//        verify<IComicListView>(comicView, times(1)).onLoadingStateChange(any())
//        verify<IComicListView>(comicView, times(1)).onComicsLoaded(any())
//
//        verify<IComicsLocalRepository>( comicsLocalRepository, times( 1)).getComicList()
//        verify<IComicsLocalRepository>( comicsLocalRepository, times( 0)).getComic(any())
//        verify<IComicsLocalRepository>( comicsLocalRepository, times( 0)).updateComic(any())
//        verify<IComicsLocalRepository>( comicsLocalRepository, times( 1)).updateComicList(any())
//
//        verify<IMarvelsRemoteRepository>( mMarvelsRemoteRepository, times( 0)).getComicDetails( any() )
//        verify<IMarvelsRemoteRepository>( mMarvelsRemoteRepository, times( 1)).getComicsList( any() )
//    }
//
//    /*
//    Given:
//    Remote repo available
//    Local repo empty
//
//    When:
//    List screen loads
//
//    Then:
//    List returned to view
//    List writen to db
//
//     */
//}
